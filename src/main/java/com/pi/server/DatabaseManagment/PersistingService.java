package com.pi.server.DatabaseManagment;

import com.pi.server.DatabaseManagment.OpenWeatherDB.DAO_OpenWeather;
import com.pi.server.DatabaseManagment.OrganisationsappDB.DAO_Organisationsapp;
import com.pi.server.Models.OpenWeather.WeatherForecast_daily_entity;
import com.pi.server.Models.OpenWeather.WeatherForecast_hourly_entity;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.Organisationsapp.Nutzer_entity;
import com.pi.server.Models.Organisationsapp.Termin_FirebaseCrypt;
import com.pi.server.Models.Organisationsapp.Token_FirebaseMessagingOrganisationsApp_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PersistingService {

    private static final String LOG_TAG = PersistingService.class.getSimpleName();

    @Autowired
    @Qualifier("DAO_MariaDB_CurrentWeather_Impl")
    private DAO_Basic dao_current;
    public final static int CurrentWeather  = 0;

    @Autowired
    @Qualifier("DAO_MariaDB_HourlyWeather_Impl")
    private DAO_Basic dao_hourly;
    @Autowired
    @Qualifier("DAO_MariaDB_HourlyWeather_Impl")
    private DAO_OpenWeather dao_hourly_second;
    public final static int HourlyWeather  = 1;

    @Autowired
    @Qualifier("DAO_MariaDB_DailyWeather_Impl")
    private DAO_Basic dao_daily;
    @Autowired
    @Qualifier("DAO_MariaDB_DailyWeather_Impl")
    private DAO_OpenWeather dao_daily_second;
    public final static int DailyWeather  = 2;

    @Autowired
    @Qualifier("DAO_MariaDB_NutzerOrganisationsapp_Impl")
    private DAO_Basic dao_nutzerOrganisationsapp;
    public final static int NutzerOrganisationsapp = 3;
    @Autowired
    @Qualifier("DAO_MariaDB_NutzerOrganisationsapp_Impl")
    private DAO_Organisationsapp dao_nutzerOrganisationsapp_second;
    public final static int NutzerOrganisationsapp_withNutzerName = 4;

    @Autowired
    @Qualifier("DAO_MariaDB_TokenOrganisationsapp_Impl")
    private DAO_Basic dao_tokenOrganisationsapp;
    public final static int NutzerOrganisationsapp_Token = 5;

    @Autowired
    @Qualifier("DAO_MariaDB_TerminOrganisationsapp_Impl")
    private DAO_Basic dao_terminOrganisationsapp;
    public final static int NutzerOrganisationsapp_Termin = 6;
    @Autowired
    @Qualifier("DAO_MariaDB_TerminOrganisationsapp_Impl")
    private DAO_Organisationsapp dao_terminOrganisationsapp_second;
    public final static int NutzerOrganisationsapp_giveTermineInTimeframe = 7;

    public void save (Object obj){
        if (obj instanceof Weather_current_entity)
            dao_current.save(obj);
        else if(obj instanceof Nutzer_entity)
            dao_nutzerOrganisationsapp.save(obj);
        else if(obj instanceof Token_FirebaseMessagingOrganisationsApp_entity)
            dao_tokenOrganisationsapp.save(obj);
        else if(obj instanceof Termin_FirebaseCrypt)
            dao_terminOrganisationsapp.save(obj);
        else
            System.out.println(LOG_TAG + " No known Object to save found!");
    }

    public void update (Object old_obj, Object new_obj){
        if (old_obj instanceof Termin_FirebaseCrypt)
            dao_terminOrganisationsapp.update(old_obj, new_obj);
        else
            System.out.println(LOG_TAG + " No known Object to save found!");
    }

    public Object get(String id, int requestedType){
        if (requestedType == CurrentWeather)
            return dao_current.get(id);
        else if (requestedType == HourlyWeather)
            return dao_hourly.get(id);
        else if (requestedType == DailyWeather)
            return dao_daily.get(id);
        else if (requestedType == NutzerOrganisationsapp)
            return dao_nutzerOrganisationsapp.get(id);
        else if (requestedType == NutzerOrganisationsapp_withNutzerName)
            return dao_nutzerOrganisationsapp_second.get_withNutzerName(id);
        else{
            System.out.println(LOG_TAG + " No known Object to get from Database requested");
            return null;
        }
    }

    public List<Object> getAllTermineInTimeframe(long startTime, long endTime){
        return dao_terminOrganisationsapp_second.getAll_withStartAndEndTime(startTime, endTime);
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

    public void saveListOfData(List obj){
        if (obj.get(0) instanceof WeatherForecast_hourly_entity)
            dao_hourly_second.saveListOfDataAndDeleteFormerData(obj);
        else if(obj.get(0) instanceof WeatherForecast_daily_entity)
            dao_daily_second.saveListOfDataAndDeleteFormerData(obj);
        else
            System.out.println(LOG_TAG + " No known Object to save found!");
    }

    public void delete(Object obj) {
        if(obj instanceof Nutzer_entity)
            dao_nutzerOrganisationsapp.delete(obj);
        else if(obj instanceof Token_FirebaseMessagingOrganisationsApp_entity)
            dao_tokenOrganisationsapp.delete(obj);
        else if(obj instanceof Termin_FirebaseCrypt)
            dao_terminOrganisationsapp.delete(obj);
        else {
            System.out.println(LOG_TAG + " No known Object to get from Database requested");
        }
    }
}
