package com.pi.server.AndroidOrganisationsapp;

import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.messaging.*;
import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.Firebase.FirebaseInitializationService;
import com.pi.server.Models.Organisationsapp.Nutzer_FirebaseCrypt;
import com.pi.server.Models.Organisationsapp.Nutzer_entity;
import com.pi.server.Models.Organisationsapp.Termin_FirebaseCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingServiceOrganisationsapp {

    private final Logger log = LoggerFactory.getLogger(FirebaseMessagingServiceOrganisationsapp.class);

    @Autowired
    PersistingService persistingService;

    public FirebaseMessagingServiceOrganisationsapp() {
        initializeDatabaseListener_organisationsapp(FirebaseInitializationService.getFirestoreInstanceOf(FirebaseInitializationService.DATABASE_ORGANISATIONSAPP), FirebaseInitializationService.FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION);
        initializeDatabaseListener_organisationsapp(FirebaseInitializationService.getFirestoreInstanceOf(FirebaseInitializationService.DATABASE_ORGANISATIONSAPP), FirebaseInitializationService.FIRESTORE_ORGANISATIONSAPP_TASK_COLLECTION_SINGLE);
        initializeDatabaseListener_organisationsapp(FirebaseInitializationService.getFirestoreInstanceOf(FirebaseInitializationService.DATABASE_ORGANISATIONSAPP), FirebaseInitializationService.FIRESTORE_ORGANISATIONSAPP_TERMIN_COLLECTION_REPETITION);
        initializeDatabaseListener_organisationsapp(FirebaseInitializationService.getFirestoreInstanceOf(FirebaseInitializationService.DATABASE_ORGANISATIONSAPP), FirebaseInitializationService.FIRESTORE_ORGANISATIONSAPP_TERMIN_COLLECTION_SINGLE);
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

                if (collectionIdentifier.equals(FirebaseInitializationService.FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION))
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
                    //sendMessage(currentTermin);
                    break;
                case MODIFIED:
                    log.info("Modified, Termin: {}", currentTermin.gibName());
                    //sendMessage(currentTermin);
                    break;
                case REMOVED:
                    log.info("Removed, Termin: {}", currentTermin.gibName());
                    //sendMessage(currentTermin);
                    break;
                default:
                    break;
            }
        }
    }

    private void nutzerAndTokenManagment(QuerySnapshot snapshots){
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            Nutzer_FirebaseCrypt currentNutzer = dc.getDocument().toObject(Nutzer_FirebaseCrypt.class);
            switch (dc.getType()) {
                case ADDED:
                    log.info("Added, Token: {}", currentNutzer.gibName());

                    persistingService.save(new Nutzer_entity(currentNutzer.gibId(), currentNutzer.gibName(), currentNutzer.gibFirebaseMessagingNutzerTokenList()));
                    break;
                case MODIFIED:
                    log.info("Modified, Token: {}", currentNutzer.gibName());
                    break;
                case REMOVED:
                    log.info("Removed, Token: {}", currentNutzer.gibName());
                    break;
                default:
                    break;
            }
        }
    }

    private void sendMessage(Termin_FirebaseCrypt currentTermin){

        // This registration token comes from the client FCM SDKs.
        String registrationToken = "fdyJZ-mxQKqcO8vc__tj3p:APA91bGmUPY8T65nuZMAQBeNBLVPF5eHmj4CA7jkoDwUFvK5hr7UMNcz_vJ5GN1LiVHK_InxqbydoAQmlmhFrubCeV695ePNtie2iaIL9FL5qTwN6mUTaatCoOutLhdEOVo6gXIc6nfN";

        AndroidNotification androidNotification = AndroidNotification.builder()
                .setTitle("Title")
                .setBody("Was geht")
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(androidNotification)
                .build();

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setToken(registrationToken)
                .setAndroidConfig(androidConfig)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = null;
        try {
            response = FirebaseMessaging.getInstance(FirebaseApp.getInstance(FirebaseInitializationService.DATABASE_ORGANISATIONSAPP)).send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        // Response is a message ID string.
        //System.out.println("Successfully sent message: " + response);
    }
}
