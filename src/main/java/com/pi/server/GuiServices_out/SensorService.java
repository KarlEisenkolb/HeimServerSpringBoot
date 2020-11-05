package com.pi.server.GuiServices_out;

import com.pi.server.DatabaseManagment.PersistingService_Sensors;
import com.pi.server.DatabaseManagment.PersistingService_Weather;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.SensorModels.Mav_XYPlotData;
import com.pi.server.Models.SensorModels.BME280.Sensor_BME280_entity;
import com.pi.server.Models.SensorModels.BME680.Sensor_BME680_entity;
import com.pi.server.Models.SensorModels.Particle.Sensor_Particle_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

import static com.pi.server.DatabaseManagment.PersistingService_Sensors.LOCATION_BAD;
import static com.pi.server.DatabaseManagment.PersistingService_Sensors.LOCATION_BIBLIOTHEK;
import static com.pi.server.DatabaseManagment.PersistingService_Weather.CurrentWeather;
import static com.pi.server.Models.SensorModels.BME280.Sensor_BME280_entity.SENSOR_BME280;
import static com.pi.server.Models.SensorModels.BME680.Sensor_BME680_entity.SENSOR_BME680;
import static com.pi.server.Models.SensorModels.Particle.Sensor_Particle_entity.SENSOR_PARTICLE;

@Service
public class SensorService {

    @Autowired
    private PersistingService_Sensors persistingService_sensors;

    @Autowired
    private PersistingService_Weather persistingService_weather;

    long timeIntervall = 36*60*60*1000; // 36h in millis

    public SensorService(){}

    public List getBme680Content(String location){
        long currentTimeInMillis = System.currentTimeMillis();
        for(String nameOfSensor : PersistingService_Sensors.sensorLocations.get(location))
            if (nameOfSensor.contains(SENSOR_BME680))
                return persistingService_sensors.getAll_withStartAndEndTime_withTableName(currentTimeInMillis-timeIntervall, currentTimeInMillis, SENSOR_BME680, location);
        return null;
    }

    public List getParticleContent(String location){
        long currentTimeInMillis = System.currentTimeMillis();
        for(String nameOfSensor : PersistingService_Sensors.sensorLocations.get(location))
            if (nameOfSensor.contains(SENSOR_PARTICLE))
                return persistingService_sensors.getAll_withStartAndEndTime_withTableName(currentTimeInMillis-timeIntervall, currentTimeInMillis, SENSOR_PARTICLE, location);
        return null;
    }

    public List getCurrentWeatherData(){
        long currentTimeInMillis = System.currentTimeMillis();
        return persistingService_weather.getAll_withStartAndEndTime(currentTimeInMillis-timeIntervall, currentTimeInMillis, CurrentWeather);
    }

    public Object getLatestSensorDataForGuiUpdate(String location) {
        Sensor_BME280_entity bme280Data             = (Sensor_BME280_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME280, LOCATION_BAD);
        Sensor_BME680_entity bme680Data             = (Sensor_BME680_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME680, LOCATION_BIBLIOTHEK);
        Sensor_Particle_entity particleData         = (Sensor_Particle_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_PARTICLE, LOCATION_BIBLIOTHEK);
        Weather_current_entity currentWeatherData   = (Weather_current_entity) persistingService_weather.getLastItem(CurrentWeather);

        HashMap<String, Object> map = new HashMap<>();

        // Daten zum aktualiseren der Plots
        map.put("latest_pm25", new Mav_XYPlotData(particleData.getId(), particleData.getPm25()));
        map.put("latest_pm10", new Mav_XYPlotData(particleData.getId(), particleData.getPm10()));
        map.put("latest_iaq", new Mav_XYPlotData(bme680Data.getId(), bme680Data.getIaq()));

        map.put("latest_680temp", new Mav_XYPlotData(bme680Data.getId(), bme680Data.getTemp()));
        map.put("latest_680relhum", new Mav_XYPlotData(bme680Data.getId(), bme680Data.getRel_hum()));
        map.put("latest_aussentemp", new Mav_XYPlotData(currentWeatherData.getRequestTimestamp(), currentWeatherData.getTemp()));

        // zusätzliche Daten für die Tabelle
        map.put("latest_680abshum", new Mav_XYPlotData(bme680Data.getId(), bme680Data.getAbs_hum()));
        map.put("latest_280temp", new Mav_XYPlotData(bme280Data.getId(), bme280Data.getTemp()));
        map.put("latest_280relhum", new Mav_XYPlotData(bme280Data.getId(), bme280Data.getRel_hum()));
        map.put("latest_280abshum", new Mav_XYPlotData(bme280Data.getId(), bme280Data.getAbs_hum()));

        /*
        // Statusbericht Sensoren
        String sensor_report;
        int sensor_status;



        map.put("sensor_report", );
        map.put("sensor_status", "");

        // Statusbericht VOC
        String voc_report;
        int voc_status;

        double iaq = bme680Data.getIaq();
        if (iaq < 150){
            voc_report = "Luftqualität gut, IAQ unter 150";
            voc_status = 0;
        }else if (iaq <200){
            voc_report = "Luftqualität ok, IAQ unter 200";
            voc_status = 1;
        }else{
            voc_report = "Luftqualität schlecht, IAQ über 200, Lüften!";
            voc_status = 2;
        }

        map.put("voc_report", voc_report);
        map.put("voc_status", voc_status);

        // Statusbericht Humidity
        String hum_report;
        int hum_status;

        map.put("hum_report", );
        map.put("hum_status", );

        // Statusbericht Partikel
        String particle_report;
        int particle_status;

        double iaq = bme680Data.getIaq();
        if (iaq < 150){
            voc_report = "Luftqualität gut, IAQ unter 150";
            voc_status = 0;
        }else if (iaq <200){
            voc_report = "Luftqualität ok, IAQ unter 200";
            voc_status = 1;
        }else{
            voc_report = "Luftqualität schlecht, IAQ über 200, Lüften!";
            voc_status = 2;
        }

        map.put("particle_report", );
        map.put("particle_status", );
        */
        return map;
    }
}
