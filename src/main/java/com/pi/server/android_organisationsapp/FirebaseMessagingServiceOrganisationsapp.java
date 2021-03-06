package com.pi.server.android_organisationsapp;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.messaging.*;
import com.pi.server.database.PersistingService_Organisationsapp;
import com.pi.server.firebase.FirebaseInitialization;
import com.pi.server.gui_services_out.ServerDataStatusService;
import com.pi.server.models.organisationsapp.FirebaseCrypt_Nutzer;
import com.pi.server.models.organisationsapp.OrganisationsApp_Nutzer_entity;
import com.pi.server.models.organisationsapp.FirebaseCrypt_Termin_entity;
import com.pi.server.models.organisationsapp.Token_FirebaseMessagingOrganisationsApp_entity;
import com.pi.server.security_handling.Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;

import static com.pi.server.database.PersistingService_Organisationsapp.NutzerOrganisationsapp;
import static com.pi.server.database.PersistingService_Organisationsapp.NutzerOrganisationsapp_withNutzerNameAsId;
import static com.pi.server.models.organisationsapp.FirebaseCrypt_Termin_entity.*;
import static com.pi.server.security_handling.Crypt.CRYPT_USE_DEFAULT_KEY;

@Service
public class FirebaseMessagingServiceOrganisationsapp {

    private final Logger log = LoggerFactory.getLogger(FirebaseMessagingServiceOrganisationsapp.class);
    
    private boolean serverBootSituation = true;
    private int countOfListenersStartedAfterBoot = 0;
    private int numberOfTerminListener;

    @Autowired
    PersistingService_Organisationsapp persistingService;

    @Autowired
    ServerDataStatusService serverDataStatusService;

    private Firestore firestore;

    public FirebaseMessagingServiceOrganisationsapp() {

        numberOfTerminListener = 3;
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
                    log.info("Listen failed: " + e);
                    return;
                }

                if (collectionIdentifier.equals(FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION))
                    nutzerAndTokenManagment(snapshots);
                else
                    terminManagment(snapshots);
                if(!serverBootSituation)
                    serverDataStatusService.setNewServerStatusAfterDataChange();
            }
        });
    }

    private void terminManagment(QuerySnapshot snapshots){
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            FirebaseCrypt_Termin_entity currentTermin = dc.getDocument().toObject(FirebaseCrypt_Termin_entity.class);
            switch (dc.getType()) {
                case ADDED:
                    log.info("Added, Termin: {}", currentTermin.gibName());
                    persistingService.save(currentTermin);
                    sendPushNotification(currentTermin, DocumentChange.Type.ADDED);
                    break;
                case MODIFIED:
                    log.info("Modified, Termin: {}", currentTermin.gibName());
                    persistingService.update(currentTermin, currentTermin);
                    sendPushNotification(currentTermin, DocumentChange.Type.MODIFIED); //Aktuell nicht erfasst wer den Termin modifiziert hat.. Nur Aufgaben werden modifziert (abgehakt)
                    break;
                case REMOVED:
                    log.info("Removed, Termin: {}", currentTermin.gibName());
                    persistingService.delete(currentTermin);
                    sendPushNotification(currentTermin, DocumentChange.Type.REMOVED); //Aktuell nicht erfasst wer den Termin gelöscht hat.. alle werden benachrichtigt
                    break;
                default:
                    break;
            }
        }
        if (serverBootSituation) {
            countOfListenersStartedAfterBoot++;
            if (countOfListenersStartedAfterBoot == numberOfTerminListener)
                serverBootSituation = false;
        }
    }

    private void nutzerAndTokenManagment(QuerySnapshot snapshots){
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            FirebaseCrypt_Nutzer currentNutzer = dc.getDocument().toObject(FirebaseCrypt_Nutzer.class);
            OrganisationsApp_Nutzer_entity nutzerMariaDB = (OrganisationsApp_Nutzer_entity) persistingService.get(currentNutzer.gibId(), NutzerOrganisationsapp);

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
                    log.info("Removed, Nutzer: {}", currentNutzer.gibName());
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

    private void ManagingTokenAndNutzer(FirebaseCrypt_Nutzer currentNutzer, OrganisationsApp_Nutzer_entity nutzerMariaDB) {
        Crypt crypt = new Crypt(CRYPT_USE_DEFAULT_KEY);
        if (nutzerMariaDB == null) {
            log.info("Added, Nutzer From firebase Not Found in Maria and Added: {} | with {} Tokens", currentNutzer.gibName(), currentNutzer.gibFirebaseMessagingNutzerTokenList().size());
            persistingService.save(new OrganisationsApp_Nutzer_entity(currentNutzer.gibId(), currentNutzer.gibName(), currentNutzer.getiFwpcRndwlS()));
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
                    log.info("Added OR Modified, Token aus firebase wurde nicht gefunden und Datenbank hinzugefuegt: {} | {}", currentNutzer.gibName(), tokenFirebase);
                }
            }
        }
    }

    private void sendPushNotification(FirebaseCrypt_Termin_entity currentTermin, Enum type) {
        if (serverBootSituation)
            return;
        else{
            OrganisationsApp_Nutzer_entity terminBesitzer_entityToContact = (OrganisationsApp_Nutzer_entity) persistingService.get(currentTermin.gibBesitzer(), NutzerOrganisationsapp_withNutzerNameAsId);
            for (Token_FirebaseMessagingOrganisationsApp_entity tokenEntity : terminBesitzer_entityToContact.getTokens())
                buildNotificationAndSend(currentTermin, tokenEntity, type);

            for (String nutzerToContact : currentTermin.gibSharedTerminNutzerList()) {
                OrganisationsApp_Nutzer_entity nutzer_entityToContact = (OrganisationsApp_Nutzer_entity) persistingService.get(nutzerToContact, NutzerOrganisationsapp_withNutzerNameAsId);
                for (Token_FirebaseMessagingOrganisationsApp_entity tokenEntity : nutzer_entityToContact.getTokens())
                    buildNotificationAndSend(currentTermin, tokenEntity, type);
            }
        }
    }

    private void buildNotificationAndSend(FirebaseCrypt_Termin_entity termin, Token_FirebaseMessagingOrganisationsApp_entity tokenEntity, Enum type){
        AndroidNotification androidNotification = AndroidNotification.builder()
                .setTitle(getTitleString(termin, type))
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
            log.info("Sended PushMessage to: {} | {} | Token: {}", tokenEntity.getNutzer_entity().getName() , termin.gibName(), tokenEntity.getToken());
        } catch (FirebaseMessagingException e) {
            ExceptionHandlingError404(tokenEntity, e);
        }

        // Response is a message ID string.
        //log.info("Successfully sent message: " + response);
    }

    private void ExceptionHandlingError404(Token_FirebaseMessagingOrganisationsApp_entity tokenEntity, FirebaseMessagingException e) {
        log.warn("FIREBASE ERROR CODE: " + e.getErrorCode());
        e.printStackTrace();
        persistingService.delete(tokenEntity);
        DocumentReference docRef = firestore.collection(FirebaseInitialization.FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION).document(tokenEntity.getNutzer_entity().getFirebaseID());
        ApiFuture<WriteResult> writeResult = docRef.update(FirebaseCrypt_Nutzer.TOKEN_MESSAGING_LIST, FieldValue.arrayRemove(tokenEntity.getToken_CryptFirebase()));
        try {
            log.info("Update time : " + writeResult.get());
        } catch (InterruptedException | ExecutionException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private String getTitleString(FirebaseCrypt_Termin_entity termin, Enum type){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String terminType;
        if (type == DocumentChange.Type.ADDED) {
            switch ((int) termin.gibType()) {
                case TYPE_AUFGABE:
                    terminType = "Neue Aufgabe:";
                    break;
                case TYPE_TERMIN:
                    terminType = "Neuer Termin:";
                    break;
                case TYPE_GEBURTSTAG:
                    terminType = "Neuer Geburtstag:";
                    break;
                case TYPE_URLAUB:
                    terminType = "Neuer Urlaub:";
                    break;
                case TYPE_SCHULFERIEN:
                    terminType = "Neue Schulferien:";
                    break;
                case TYPE_FEIERTRAG:
                    terminType = "Neuer Feiertag:";
                    break;
                default:
                    terminType = "NO TYPE";
                    break;
            }
        } else if (type == DocumentChange.Type.MODIFIED) {
            long terminErledigungsTime          = termin.gibErledigungsTime();
            if (terminErledigungsTime == FirebaseCrypt_Termin_entity.TASK_NOT_DONE)
                terminType = "Aufgabe noch nicht erledigt:";
            else
                terminType = "Aufgabe erledigt um " +timeFormatter.format(terminErledigungsTime);
        }else{
            switch ((int) termin.gibType()) {
                case TYPE_AUFGABE:
                    terminType = "Aufgabe gelöscht:";
                    break;
                case TYPE_TERMIN:
                    terminType = "Termin gelöscht:";
                    break;
                case TYPE_GEBURTSTAG:
                    terminType = "Geburtstag gelöscht:";
                    break;
                case TYPE_URLAUB:
                    terminType = "Urlaub gelöscht:";
                    break;
                case TYPE_SCHULFERIEN:
                    terminType = "Schulferien gelöscht:";
                    break;
                case TYPE_FEIERTRAG:
                    terminType = "Feiertag gelöscht:";
                    break;
                default:
                    terminType = "NO TYPE";
                    break;
            }
        }

        return terminType + " " + termin.gibName() ;
    }
    private String getBodyString(FirebaseCrypt_Termin_entity termin){
        long terminStartTime                = termin.gibStartTimeInMillis();
        long terminEndTimeOnDay             = termin.gibEndTimeInMillisOnDay();

        ZonedDateTime cStart = ZonedDateTime.ofInstant(Instant.ofEpochMilli(terminStartTime), ZoneId.systemDefault());
        ZonedDateTime cEnd = ZonedDateTime.ofInstant(Instant.ofEpochMilli(terminEndTimeOnDay), ZoneId.systemDefault());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE d. MMM");

        String terminTime;
        if (cStart.getHour() == 0 && cStart.getMinute() == 0 && cEnd.getHour() == 0 && cEnd.getMinute() == 0) //Falls Start und Endzeit = Default (O Uhr) dann nicht anzeigen
            terminTime = "";
        else if(cEnd.getHour() == 0 && cEnd.getMinute() == 0) // Startzeit am Tag gesetzt aber Endzeit ist Default => Nur Startzeit zeigen
            terminTime = timeFormatter.format(termin.gibStartTimeInMillis());
        else
            terminTime = timeFormatter.format(termin.gibStartTimeInMillis()) + "-" + timeFormatter.format(termin.gibEndTimeInMillisOnDay());

        return "erstellt von " + termin.gibBesitzer() + " am " + dateFormatter.format(termin.gibStartTimeInMillis()) + " " + terminTime;
    }
}
