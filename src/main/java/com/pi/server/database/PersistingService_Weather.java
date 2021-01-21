package com.pi.server.database;

import com.pi.server.database.open_weather.DAO_Weather;
import com.pi.server.gui_controller_out.MainController;
import com.pi.server.models.open_weather.WeatherForecast_daily_entity;
import com.pi.server.models.open_weather.WeatherForecast_hourly_entity;
import com.pi.server.models.open_weather.Weather_current_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PersistingService_Weather {

    private final Logger log = LoggerFactory.getLogger(PersistingService_Weather.class);

    @Autowired
    @Qualifier("DAO_MariaDB_CurrentWeather_Impl")
    private DAO_Weather dao_current;
    public final static int CurrentWeather  = 0;

    @Autowired
    @Qualifier("DAO_MariaDB_HourlyWeather_Impl")
    private DAO_Weather dao_hourly;
    public final static int HourlyWeather  = 1;

    @Autowired
    @Qualifier("DAO_MariaDB_DailyWeather_Impl")
    private DAO_Weather dao_daily;
    public final static int DailyWeather  = 2;

    public Object getLastItem(int requestedType){
        if (requestedType == CurrentWeather)
            return dao_current.getLastItem();
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }

    public List<Object> getAll_withStartAndEndTime(long startTime, long endTime, int requestedType){
        if (requestedType == CurrentWeather)
            return dao_current.getAll_withStartAndEndTime(startTime, endTime);
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }

    public List<Object> getAll(int requestedType){
        if (requestedType == CurrentWeather)
            return dao_current.getAll();
        else if (requestedType == HourlyWeather)
            return dao_hourly.getAll();
        else if (requestedType == DailyWeather)
            return dao_daily.getAll();
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }

    public void save (Object obj){
        if (obj instanceof Weather_current_entity)
            dao_current.save(obj);
        else
            log.warn(" No known Object to save found!");
    }

    public void saveListOfDataAndDeleteFormerData(List obj){
        if (obj.get(0) instanceof WeatherForecast_hourly_entity)
            dao_hourly.saveListOfDataAndDeleteFormerData(obj);
        else if(obj.get(0) instanceof WeatherForecast_daily_entity)
            dao_daily.saveListOfDataAndDeleteFormerData(obj);
        else
            log.warn(" No known Object to save found!");
    }

}
