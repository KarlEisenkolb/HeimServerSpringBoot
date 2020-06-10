package com.pi.server.GuiServices_out;


import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.Models.OpenWeather.WeatherForecast_daily_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    @Autowired
    private PersistingService persistingService;

    public MainService(){}

    @EventListener(ApplicationReadyEvent.class)
    public void doWhenReady(){

    }

    public String getTimeAndDateString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm 'Uhr |' EEE d MMM ");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    public List<String> getDateHeadingStrings(List dayData){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE d MMM");
        List<String> dateList = new ArrayList<>();
        for (WeatherForecast_daily_entity dailyItem : (List<WeatherForecast_daily_entity>)dayData)
            dateList.add(simpleDateFormat.format(dailyItem.getTime()*1000));
        return dateList;
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

        return persistingService.getAll(PersistingService.CurrentWeather);
    }

    public List getWeatherHourlyForecastContent(){
        return persistingService.getAll(PersistingService.HourlyWeather);
    }

    public List getWeatherDailyForecastContent(){
        return persistingService.getAll(PersistingService.DailyWeather);
    }
}
