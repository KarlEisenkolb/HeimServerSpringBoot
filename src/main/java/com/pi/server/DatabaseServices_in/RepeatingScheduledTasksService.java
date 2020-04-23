package com.pi.server.DatabaseServices_in;

import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.HttpRequests.CustomHttpRequest;
import com.pi.server.HttpRequests.ExtractJsonData;
import com.pi.server.Models.Weather_current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RepeatingScheduledTasksService {

    private static final Logger log = LoggerFactory.getLogger(RepeatingScheduledTasksService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final String OpenWeatherAPI_KEY = "&APPID=87f30450c746934858267678961b818c";
    private final String OpenWeatherAPI_BASE_REQUEST_FORECAST = "https://api.openweathermap.org/data/2.5/onecall?";
    private final String OpenWeatherAPI_COORDINATES_lat_lon ="lat=48.7433425&lon=9.3201122"; // Längen- und Breitengrad Esslingen a.N.
    private final String stringUrl = OpenWeatherAPI_BASE_REQUEST_FORECAST + OpenWeatherAPI_COORDINATES_lat_lon + OpenWeatherAPI_KEY;

    @Autowired
    PersistingService persistingService;

    @Scheduled(fixedRate = 1000*60*5)
    public void openWeatherHttpRequest() {
        log.info("OpenWeather Http-Request, One-Call-Api {}", dateFormat.format(new Date()));

        String jsonResponse = CustomHttpRequest.startRequestInstance(stringUrl);
        Weather_current weather_current = ExtractJsonData.extractData_current(jsonResponse);
        persistingService.save(weather_current);
    }

}
