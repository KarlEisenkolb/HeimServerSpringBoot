package com.pi.server.DatabaseManagment;

import com.pi.server.Models.OpenWeather.WeatherForecast_daily_entity;
import com.pi.server.Models.OpenWeather.WeatherForecast_hourly_entity;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.Organisationsapp.Nutzer_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PersistingService {

    private static final String LOG_TAG = PersistingService.class.getSimpleName();

    @Autowired
    @Qualifier("DAO_MariaDB_CurrentWeather_Impl")
    private DAO dao_current;
    public final static int CurrentWeather  = 0;

    @Autowired
    @Qualifier("DAO_MariaDB_HourlyWeather_Impl")
    private DAO dao_hourly;
    public final static int HourlyWeather  = 1;

    @Autowired
    @Qualifier("DAO_MariaDB_DailyWeather_Impl")
    private DAO dao_daily;
    public final static int DailyWeather  = 2;

    @Autowired
    @Qualifier("DAO_MariaDB_NutzerOrganisationsapp_Impl")
    private DAO dao_nutzerOrganisationsapp;
    public final static int NutzerOrganisationsapp  = 3;

    public void save (Object obj){
        if (obj instanceof Weather_current_entity)
            dao_current.save(obj);
        else if(obj instanceof Nutzer_entity)
            dao_nutzerOrganisationsapp.save(obj);
        else
            System.out.println(LOG_TAG + " No known Object to save found!");
    }

    public List<Object> getAll(int requestedType){
        if (requestedType == CurrentWeather)
            return dao_current.getAll();
        else if (requestedType == HourlyWeather)
            return dao_hourly.getAll();
        else if (requestedType == DailyWeather)
            return dao_daily.getAll();
        else if (requestedType == NutzerOrganisationsapp)
            return dao_nutzerOrganisationsapp.getAll();
        else{
            System.out.println(LOG_TAG + " No known Object to get from Database requested");
            return null;
        }
    }

    public void saveListOfData (List obj){
        if (obj.get(0) instanceof WeatherForecast_hourly_entity)
            dao_hourly.saveListOfDataAndDeleteFormerData(obj);
        else if(obj.get(0) instanceof WeatherForecast_daily_entity)
            dao_daily.saveListOfDataAndDeleteFormerData(obj);
        else if(obj.get(0) instanceof Nutzer_entity)
            dao_nutzerOrganisationsapp.saveListOfDataAndDeleteFormerData(obj);
        else
            System.out.println(LOG_TAG + " No known Object to save found!");
    }
}
