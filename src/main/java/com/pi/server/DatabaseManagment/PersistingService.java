package com.pi.server.DatabaseManagment;

import com.pi.server.DatabaseManagment.OpenWeatherDB.DAO_OpenWeather;
import com.pi.server.DatabaseManagment.OrganisationsappDB.DAO_Organisationsapp;
import com.pi.server.DatabaseManagment.SensorsDB.DAO_Sensors;
import com.pi.server.Models.OpenWeather.WeatherForecast_daily_entity;
import com.pi.server.Models.OpenWeather.WeatherForecast_hourly_entity;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.Organisationsapp.OrganisationsApp_Nutzer_entity;
import com.pi.server.Models.Organisationsapp.FirebaseCrypt_Termin_entity;
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

    @Autowired
    @Qualifier("DAO_MariaDB_BME680_Impl")
    private DAO_Sensors dao_bme680;
    public final static int bme680_data = 8;

    @Autowired
    @Qualifier("DAO_MariaDB_Particle_Impl")
    private DAO_Sensors dao_particle;
    public final static int particle_data = 9;

    public void save (Object obj){
        if (obj instanceof Weather_current_entity)
            dao_current.save(obj);
        else if(obj instanceof OrganisationsApp_Nutzer_entity)
            dao_nutzerOrganisationsapp.save(obj);
        else if(obj instanceof Token_FirebaseMessagingOrganisationsApp_entity)
            dao_tokenOrganisationsapp.save(obj);
        else if(obj instanceof FirebaseCrypt_Termin_entity)
            dao_terminOrganisationsapp.save(obj);
        else
            System.out.println(LOG_TAG + " No known Object to save found!");
    }

    public void update (Object old_obj, Object new_obj){
        if (old_obj instanceof FirebaseCrypt_Termin_entity)
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

    public List<Object> getAllInTimeframe(int requestedType, long startTime, long endTime){
        if (requestedType == NutzerOrganisationsapp_giveTermineInTimeframe)
            return dao_terminOrganisationsapp_second.getAll_withStartAndEndTime(startTime, endTime);
        else if (requestedType == bme680_data)
            return dao_bme680.getAll_withStartAndEndTime(startTime, endTime);
        else if (requestedType == particle_data)
            return dao_particle.getAll_withStartAndEndTime(startTime, endTime);
        else{
            System.out.println(LOG_TAG + " No known Object to get from Database requested");
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
        else if (requestedType == NutzerOrganisationsapp)
            return dao_nutzerOrganisationsapp.getAll();
        /*else if (requestedType == bme680_data)
            return dao_bme680.getAll();
        else if (requestedType == particle_data)
            return dao_particle.getAll();*/
        else{
            System.out.println(LOG_TAG + " No known Object to get from Database requested");
            return null;
        }
    }

    public void saveListOfDataAndDeleteFormerData(List obj){
        if (obj.get(0) instanceof WeatherForecast_hourly_entity)
            dao_hourly_second.saveListOfDataAndDeleteFormerData(obj);
        else if(obj.get(0) instanceof WeatherForecast_daily_entity)
            dao_daily_second.saveListOfDataAndDeleteFormerData(obj);
        else
            System.out.println(LOG_TAG + " No known Object to save found!");
    }

    public void delete(Object obj) {
        if(obj instanceof OrganisationsApp_Nutzer_entity)
            dao_nutzerOrganisationsapp.delete(obj);
        else if(obj instanceof Token_FirebaseMessagingOrganisationsApp_entity)
            dao_tokenOrganisationsapp.delete(obj);
        else if(obj instanceof FirebaseCrypt_Termin_entity)
            dao_terminOrganisationsapp.delete(obj);
        else {
            System.out.println(LOG_TAG + " No known Object to get from Database requested");
        }
    }
}
