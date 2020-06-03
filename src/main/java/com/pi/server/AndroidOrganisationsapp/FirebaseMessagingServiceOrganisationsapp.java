package com.pi.server.AndroidOrganisationsapp;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.messaging.*;
import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.Firebase.FirebaseInitialization;
import com.pi.server.Models.Organisationsapp.Nutzer_FirebaseCrypt;
import com.pi.server.Models.Organisationsapp.Nutzer_entity;
import com.pi.server.Models.Organisationsapp.Termin_FirebaseCrypt;
import com.pi.server.Models.Organisationsapp.Token_FirebaseMessagingOrganisationsApp_entity;
import com.pi.server.SecurityHandling.Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.pi.server.DatabaseManagment.PersistingService.NutzerOrganisationsapp;
import static com.pi.server.DatabaseManagment.PersistingService.NutzerOrganisationsapp_withNutzerName;
import static com.pi.server.SecurityHandling.Crypt.CRYPT_USE_DEFAULT_KEY;

@Service
public class FirebaseMessagingServiceOrganisationsapp {

    private final Logger log = LoggerFactory.getLogger(FirebaseMessagingServiceOrganisationsapp.class);
    
    boolean firstTriggerAfterServerBoot = true;

    @Autowired
    PersistingService persistingService;

    private Firestore firestore;

    public FirebaseMessagingServiceOrganisationsapp() {

        firestore = FirebaseInitialization.getFirestoreInstanceOf(FirebaseInitialization.DATABASE_ORGANISATIONSAPP);
        initializeDatabaseListener_organisationsapp(firestore, FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION);
        initializeDatabaseListener_organisationsapp(firestore, FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_TASK_COLLECTION_SINGLE);
        initializeDatabaseListener_organisationsapp(firestore, FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_TERMIN_COLLECTION_REPETITION);
        initializeDatabaseListener_organisationsapp(firestore, FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_TERMIN_COLLECTION_SINGLE);
    }

    private void initializeDatabaseListener_organisationsapp(Firestore firestore, String collectionIdentifier){
        CollectionReference col = firestore.collection(collectionIdentifier);
        col.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (collectionIdentifier.equals(FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION))
                    nutzerAndTokenManagment(snapshots);
                else
                    terminManagment(snapshots);
            }
        });
    }

    private void terminManagment(QuerySnapshot snapshots){
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            Termin_FirebaseCrypt currentTermin = dc.getDocument().toObject(Termin_FirebaseCrypt.class);
            switch (dc.getType()) {
                case ADDED:
                    log.info("Added, Termin: {}", currentTermin.gibName());
                    sendPushNotification(currentTermin);
                    break;
                case MODIFIED:
                    log.info("Modified, Termin: {}", currentTermin.gibName());
                    //sendPushNotification(currentTermin); Aktuell nicht erfasst wer den Termin modifiziert hat.. jeweils die Anderen müssen benachrichtigt werden.
                    break;
                case REMOVED:
                    log.info("Removed, Termin: {}", currentTermin.gibName());
                    //sendPushNotification(currentTermin); Aktuell nicht erfasst wer den Termin gelöscht hat.. jeweils die Anderen müssen benachrichtigt werden. Besser auf dem Client bearbeiten
                    break;
                default:
                    break;
            }
        }
    }

    private void nutzerAndTokenManagment(QuerySnapshot snapshots){
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            Nutzer_FirebaseCrypt currentNutzer = dc.getDocument().toObject(Nutzer_FirebaseCrypt.class);
            Nutzer_entity nutzerMariaDB = (Nutzer_entity) persistingService.get(currentNutzer.gibId(), NutzerOrganisationsapp);

            switch (dc.getType()) {
                case ADDED:
                    log.info("Added, Token: {} | with {} Tokens", currentNutzer.gibName(), currentNutzer.gibFirebaseMessagingNutzerTokenList().size());
                    ManagingTokenAndNutzer(currentNutzer, nutzerMariaDB);
                    break;
                case MODIFIED:
                    log.info("Modified, Token: {}", currentNutzer.gibName());
                    ManagingTokenAndNutzer(currentNutzer, nutzerMariaDB);
                    break;
                case REMOVED:
                    log.info("Removed, Token: {}", currentNutzer.gibName());
                    if (nutzerMariaDB != null)
                        persistingService.delete(nutzerMariaDB);
                    else
                        log.info("Removed, Nutzer should be removed but was not found in ServerDB: {}", currentNutzer.gibName());
                    break;
                default:
                    break;
            }
        }
    }

    private void ManagingTokenAndNutzer(Nutzer_FirebaseCrypt currentNutzer, Nutzer_entity nutzerMariaDB) {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        if (nutzerMariaDB == null) {
            log.info("Added, Nutzer From Firebase Not Found in Maria and Added: {} | with {} Tokens", currentNutzer.gibName(), currentNutzer.gibFirebaseMessagingNutzerTokenList().size());
            persistingService.save(new Nutzer_entity(currentNutzer.gibId(), currentNutzer.gibName(), currentNutzer.getiFwpcRndwlS()));
        }else { // Nutzer existiert bereits in ServerDB, jetzt wird jedes Token überprüft und notfalls geupdated
            for (String tokenFirebaseCrypt : currentNutzer.getiFwpcRndwlS()) {
                String tokenFirebase = crypt.decryptString(tokenFirebaseCrypt);
                boolean tokenFromFirebaseWasFoundInMariaDB = false;
                for (Token_FirebaseMessagingOrganisationsApp_entity tokenMariaDB : nutzerMariaDB.getTokens()) {
                    if (tokenMariaDB.getToken().equals(tokenFirebase))
                        tokenFromFirebaseWasFoundInMariaDB = true;
                }
                if (!tokenFromFirebaseWasFoundInMariaDB) {
                    persistingService.save(new Token_FirebaseMessagingOrganisationsApp_entity(tokenFirebase, tokenFirebaseCrypt, nutzerMariaDB));
                    log.info("Added OR Modified, Token aus Firebase wurde nicht gefunden und Datenbank hinzugefuegt: {} | {}", currentNutzer.gibName(), tokenFirebase);
                }
            }
        }
    }

    private void sendPushNotification(Termin_FirebaseCrypt currentTermin) {
        for (String nutzerToContact : currentTermin.gibSharedTerminNutzerList()) {
            Nutzer_entity nutzer_entityToContact = (Nutzer_entity) persistingService.get(nutzerToContact, NutzerOrganisationsapp_withNutzerName);
            for (Token_FirebaseMessagingOrganisationsApp_entity tokenEntity : nutzer_entityToContact.getTokens())
                buildNotificationAndSend(currentTermin, tokenEntity);
        }
    }

    private void buildNotificationAndSend(Termin_FirebaseCrypt termin, Token_FirebaseMessagingOrganisationsApp_entity tokenEntity){
        AndroidNotification androidNotification = AndroidNotification.builder()
                .setTitle(getTitleString(termin)) // Neuer Termin/Task "Carolin streicheln" Fr. 5.6.20 9:20-15:43
                .setBody(getBodyString(termin))
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(androidNotification)
                .build();

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setToken(tokenEntity.getToken())
                .setAndroidConfig(androidConfig)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = null;
        try {
            response = FirebaseMessaging.getInstance(FirebaseApp.getInstance(FirebaseInitialization.DATABASE_ORGANISATIONSAPP)).send(message);
        } catch (FirebaseMessagingException e) {
            System.out.println("FIREBASE ERROR CODE: " + e.getErrorCode());
            e.printStackTrace();
            persistingService.delete(tokenEntity);
            DocumentReference docRef = firestore.collection(FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION).document(tokenEntity.getNutzer_entity().getFirebaseID());
            ApiFuture<WriteResult> writeResult = docRef.update(Nutzer_FirebaseCrypt.TOKEN_MESSAGING_LIST, FieldValue.arrayRemove(tokenEntity.getToken_CryptFirebase()));
            try {
                System.out.println("Update time : " + writeResult.get());
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        // Response is a message ID string.
        //System.out.println("Successfully sent message: " + response);
    }

    private String getTitleString(Termin_FirebaseCrypt termin){

        return termin.gibName();
    }
    private String getBodyString(Termin_FirebaseCrypt termin){return "";}
}
