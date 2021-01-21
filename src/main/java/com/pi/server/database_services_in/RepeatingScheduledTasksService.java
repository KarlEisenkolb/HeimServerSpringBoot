package com.pi.server.database_services_in;

import com.pi.server.database.PersistingService_Weather;
import com.pi.server.gui_services_out.ServerDataStatusService;
import com.pi.server.http_requests.CustomHttpRequest;
import com.pi.server.http_requests.ExtractJsonData;
import com.pi.server.models.open_weather.WeatherForecast_daily_entity;
import com.pi.server.models.open_weather.WeatherForecast_hourly_entity;
import com.pi.server.models.open_weather.Weather_current_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RepeatingScheduledTasksService {

    private static final Logger log = LoggerFactory.getLogger(RepeatingScheduledTasksService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final String OpenWeatherAPI_KEY = "&APPID=87f30450c746934858267678961b818c";
    private final String OpenWeatherAPI_BASE_REQUEST_FORECAST = "https://api.openweathermap.org/data/2.5/onecall?";
    private final String OpenWeatherAPI_COORDINATES_lat_lon ="lat=48.7433425&lon=9.3201122"; // Längen- und Breitengrad Esslingen a.N.
    private final String SPRACHE_DEUTSCH = "&lang=de";
    private final String UNITS_METRIC = "&units=metric";
    private final String stringUrl = OpenWeatherAPI_BASE_REQUEST_FORECAST + OpenWeatherAPI_COORDINATES_lat_lon + SPRACHE_DEUTSCH + UNITS_METRIC + OpenWeatherAPI_KEY;

    @Autowired
    PersistingService_Weather persistingService_weather;

    @Autowired
    ServerDataStatusService serverDataStatusService;

    @Scheduled(fixedRate = 1000*60*5)
    public void openWeatherHttpRequest() {
        log.info("open_weather Http-Request, One-Call-Api {}", dateFormat.format(new Date()));

        String jsonResponse = CustomHttpRequest.startRequestInstance(stringUrl);

        Weather_current_entity weather_current = ExtractJsonData.extractData_current(jsonResponse);
        persistingService_weather.save(weather_current);

        List<WeatherForecast_hourly_entity> weatherListHourly = ExtractJsonData.extractAllData_hourly(jsonResponse);
        persistingService_weather.saveListOfDataAndDeleteFormerData(weatherListHourly);

        List<WeatherForecast_daily_entity> weatherListDaily = ExtractJsonData.extractAllData_daily(jsonResponse);
        persistingService_weather.saveListOfDataAndDeleteFormerData(weatherListDaily);

        serverDataStatusService.setNewServerStatusAfterDataChange();
    }

}
