package com.pi.server.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitialization {

    private static FirebaseInitialization fs;

    private Firestore firestoreDB_trainingsapp;
    private Firestore firestoreDB_organisationsapp;
    private Firestore firestoreDB_einkaufsapp;

    private FirebaseApp firebaseApp_organisationsapp;

    //=============================================================================================================================================================================================
    final static private String PATHNAME_JSON_TRAININGSAPP      = "./trainingsapp-e9b88-firebase-adminsdk-bf50c-17bfc29c28.json"; //File needs to be in the same directory
    final static private String DATABASE_URL_TRAININGSSAPP      = "https://trainingsapp-e9b88.firebaseio.com";
    final static public  String DATABASE_TRAININGSAPP           = "trainingsapp";

    //=============================================================================================================================================================================================
    final static private String PATHNAME_JSON_ORGANISATIONSAPP  = "./organisationsapp-fe08a-firebase-adminsdk-y9fca-1be967e329.json"; //File needs to be in the same directory
    final static private String DATABASE_URL_ORGANISATIONSAPP   = "https://organisationsapp-fe08a.firebaseio.com";
    final static public  String DATABASE_ORGANISATIONSAPP       = "organisationsapp";

    public final static String FIRESTORE_ORGANISATIONSAPP_NUTZER_COLLECTION                = "ldOeVsFLoMwSfJgdeKgeJp";
    public final static String FIRESTORE_ORGANISATIONSAPP_TASK_COLLECTION_SINGLE           = "ePnKdWhsKVhephrFGjdJgu";
    public final static String FIRESTORE_ORGANISATIONSAPP_TERMIN_COLLECTION_REPETITION     = "oFwzeSPswqBSNmsGjqSkdn";
    public final static String FIRESTORE_ORGANISATIONSAPP_TERMIN_COLLECTION_SINGLE         = "sqSkdWkslSJWldjbqdUysl";
    //=============================================================================================================================================================================================
    final static private String PATHNAME_JSON_EINKAUFSAPP       = "./einkaufsapp-8d186-firebase-adminsdk-90w7f-ce9b2960af.json"; //File needs to be in the same directory
    final static private String DATABASE_URL_EINKAUFSAPP        = "https://einkaufsapp-8d186.firebaseio.com";
    final static public  String DATABASE_EINKAUFSAPP            = "einkaufsapp";

    public static Firestore getFirestoreInstanceOf(String DatabaseIdentifier){

        if (fs == null)
            fs = new FirebaseInitialization();

        switch(DatabaseIdentifier){
            case DATABASE_TRAININGSAPP:
                if (fs.getFirestoreDB_trainingsapp() == null)
                    fs.setFirestoreDB_trainingsapp(initiateFirebaseFirestoreDatabase(PATHNAME_JSON_TRAININGSAPP, DATABASE_URL_TRAININGSSAPP, DATABASE_TRAININGSAPP));
                return fs.getFirestoreDB_trainingsapp();
            case DATABASE_ORGANISATIONSAPP:
                if (fs.getFirestoreDB_organisationsapp() == null)
                    fs.setFirestoreDB_organisationsapp(initiateFirebaseFirestoreDatabase(PATHNAME_JSON_ORGANISATIONSAPP, DATABASE_URL_ORGANISATIONSAPP, DATABASE_ORGANISATIONSAPP));
                return fs.getFirestoreDB_organisationsapp();
            case DATABASE_EINKAUFSAPP:
                if (fs.getFirestoreDB_einkaufsapp() == null)
                    fs.setFirestoreDB_einkaufsapp(initiateFirebaseFirestoreDatabase(PATHNAME_JSON_EINKAUFSAPP, DATABASE_URL_EINKAUFSAPP, DATABASE_EINKAUFSAPP));
                return fs.getFirestoreDB_einkaufsapp();
            default:
                System.out.println("ERROR NO DATABASE!");
                return null;
        }
    }

    private static Firestore initiateFirebaseFirestoreDatabase(String jsonFilePath, String databaseURL, String databaseIdentifier) {
        try {
            File auth_file = new File(jsonFilePath);
            FileInputStream serviceAccount = new FileInputStream(auth_file);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseURL)
                    .build();

            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, databaseIdentifier);
            return FirestoreClient.getFirestore(firebaseApp);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Firestore getFirestoreDB_trainingsapp() {
        return firestoreDB_trainingsapp;
    }

    public Firestore getFirestoreDB_organisationsapp() {
        return firestoreDB_organisationsapp;
    }

    public Firestore getFirestoreDB_einkaufsapp() {
        return firestoreDB_einkaufsapp;
    }

    public void setFirestoreDB_trainingsapp(Firestore firestoreDB_trainingsapp) {
        this.firestoreDB_trainingsapp = firestoreDB_trainingsapp;
    }

    public void setFirestoreDB_organisationsapp(Firestore firestoreDB_organisationsapp) {
        this.firestoreDB_organisationsapp = firestoreDB_organisationsapp;
    }

    public void setFirestoreDB_einkaufsapp(Firestore firestoreDB_einkaufsapp) {
        this.firestoreDB_einkaufsapp = firestoreDB_einkaufsapp;
    }
}
