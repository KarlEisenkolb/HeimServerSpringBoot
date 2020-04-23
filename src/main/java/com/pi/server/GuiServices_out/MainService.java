package com.pi.server.GuiServices_out;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.Models.Weather_current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Service
public class MainService {

    private final Firestore firestore;
    private final FirebaseOptions options;

    @Autowired
    private PersistingService persistingService;

    public MainService() throws Exception {
        File auth_file = new File("./trainingsapp-e9b88-firebase-adminsdk-bf50c-17bfc29c28.json"); //File needs to be in the same directory
        FileInputStream serviceAccount =
                new FileInputStream(auth_file);

        options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://trainingsapp-e9b88.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        firestore = FirestoreClient.getFirestore();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doWhenReady(){

    }

    public List getCurrentWeatherContent() {
        // Create a Map to store the data we want to set
        /*Map<String, Object> docData = new HashMap<>();
        docData.put("name", "Los Angeles");
        docData.put("state", "CA");
        docData.put("country", "humhumhu");
        docData.put("regions", Arrays.asList("west_coast", "socal"));
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        ApiFuture<WriteResult> future = firestore.collection("cities").document("LA").set(docData);

        DocumentReference document = firestore.collection("cities").document("LA");
        try {
            return document.get().get().getString("country"); // schlägt fehl!
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        return persistingService.getAll();
    }
}
