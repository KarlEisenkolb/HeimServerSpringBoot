package com.pi.server.GuiServices_out;

import com.pi.server.DatabaseManagment.PersistingService_Sensors;
import com.pi.server.DatabaseManagment.PersistingService_Weather;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.SensorModels.Mav_XYPlotData;
import com.pi.server.Models.SensorModels.BME280.Sensor_BME280_entity;
import com.pi.server.Models.SensorModels.BME680.Sensor_BME680_entity;
import com.pi.server.Models.SensorModels.Particle.Sensor_Particle_entity;
import org.mariadb.jdbc.internal.com.read.resultset.SelectResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pi.server.DatabaseManagment.PersistingService_Sensors.*;
import static com.pi.server.DatabaseManagment.PersistingService_Weather.CurrentWeather;
import static com.pi.server.Models.SensorModels.BME280.Sensor_BME280_entity.SENSOR_BME280;
import static com.pi.server.Models.SensorModels.BME680.Sensor_BME680_entity.SENSOR_BME680;
import static com.pi.server.Models.SensorModels.Particle.Sensor_Particle_entity.SENSOR_PARTICLE;
import static java.lang.Math.exp;

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

    public Object getLatestSensorDataForGuiUpdate(String locationsdsdf) {
        long currentTimeInMillis = System.currentTimeMillis();

        Sensor_BME280_entity bme280Data             = (Sensor_BME280_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME280, LOCATION_BAD);
        Sensor_BME680_entity bme680Data_bib         = (Sensor_BME680_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME680, LOCATION_BIBLIOTHEK);
        Sensor_BME680_entity bme680Data_schlaf      = (Sensor_BME680_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME680, LOCATION_SCHLAFZIMMER);
        Sensor_Particle_entity particleData         = (Sensor_Particle_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_PARTICLE, LOCATION_BIBLIOTHEK);
        Weather_current_entity currentWeatherData   = (Weather_current_entity) persistingService_weather.getLastItem(CurrentWeather);

        HashMap<String, Object> map = new HashMap<>();

        // Daten zum aktualiseren der Plots
        map.put("latest_pm25", new Mav_XYPlotData(particleData.getId(), particleData.getPm25()));
        map.put("latest_pm10", new Mav_XYPlotData(particleData.getId(), particleData.getPm10()));
        map.put("latest_iaq", new Mav_XYPlotData(bme680Data_bib.getId(), bme680Data_bib.getIaq()));

        map.put("latest_680temp", new Mav_XYPlotData(bme680Data_bib.getId(), bme680Data_bib.getTemp()));
        map.put("latest_680relhum", new Mav_XYPlotData(bme680Data_bib.getId(), bme680Data_bib.getRel_hum()));
        map.put("latest_aussentemp", new Mav_XYPlotData(currentWeatherData.getRequestTimestamp(), currentWeatherData.getTemp()));

        // zusätzliche Daten für die Tabelle
        map.put("latest_680abshum", new Mav_XYPlotData(bme680Data_bib.getId(), bme680Data_bib.getAbs_hum()));
        map.put("latest_280temp", new Mav_XYPlotData(bme280Data.getId(), bme280Data.getTemp()));
        map.put("latest_280relhum", new Mav_XYPlotData(bme280Data.getId(), bme280Data.getRel_hum()));
        map.put("latest_280abshum", new Mav_XYPlotData(bme280Data.getId(), bme280Data.getAbs_hum()));

        map.put("latest_iaq_schl", new Mav_XYPlotData(bme680Data_schlaf.getId(), bme680Data_schlaf.getIaq()));
        map.put("latest_680temp_schl", new Mav_XYPlotData(bme680Data_schlaf.getId(), bme680Data_schlaf.getTemp()));
        map.put("latest_680relhum_schl", new Mav_XYPlotData(bme680Data_schlaf.getId(), bme680Data_schlaf.getRel_hum()));
        map.put("latest_680abshum_schl", new Mav_XYPlotData(bme680Data_schlaf.getId(), bme680Data_schlaf.getAbs_hum()));

        String sensor_report = "Alle Sensoren verbunden. Daten sind aktuell";
        List<String> sensorNoContactYellowList = new ArrayList<>();
        List<String> sensorNoContactRedList = new ArrayList<>();

        String voc_report = "IAQ überall unter 150, Gute Luftqualität";
        List<String> iaqYellowList = new ArrayList<>();
        List<String> iaqRedList = new ArrayList<>();

        String hum_report = " relative Luftfeuchtigkeit überall unterhalb 60%";
        List<String> humYellowList = new ArrayList<>();
        List<String> humRedList = new ArrayList<>();

        String particle_report = "EU Grenzwerte für PM2,5 max 20&mu;g/m&sup3; und PM10 max 40&mu;g/m&sup3;  sind übererfüllt";
        List<String> particleYellowList = new ArrayList<>();
        List<String> particleRedList = new ArrayList<>();

        String sensor_color = "opacity_ok";
        String voc_color = "opacity_ok";
        String hum_color = "opacity_ok";
        String particle_color = "opacity_ok";

        for (Map.Entry<String, List<String>> entry : PersistingService_Sensors.sensorLocations.entrySet()) {
            List<String> sensorslist = entry.getValue();
            String location = entry.getKey();
            for (String sensor : sensorslist) {
                if (sensor.contains(SENSOR_BME680)) {
                    Sensor_BME680_entity bme680 = (Sensor_BME680_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME680, location);

                    if(bme680.getId() < currentTimeInMillis-1000*60*20) //Daten älter als 20 min
                        sensorNoContactRedList.add("BME680 (" + location + ") ");
                    else if (bme680.getId() < currentTimeInMillis-1000*30) //Daten älter als 30s
                        sensorNoContactYellowList.add("BME680 (" + location + ") ");

                    double iaq = bme680.getIaq();
                    if(iaq >=200)
                        iaqRedList.add(location + " (" + Math.round(iaq) + ") ");
                    else if (iaq >= 150)
                        iaqYellowList.add(location + " (" + Math.round(iaq) + ") ");

                    double temp = bme680.getTemp();
                    double absHum = currentWeatherData.getAbs_hum();
                    double psat = 611.2*exp(17.62*temp/(243.12+temp)); //psat in Pa (Magnus-Formel Wikipedia)
                    double relHum_Lueftung = absHum*461.52*(temp+273.15)/(10*psat); //relative Feuchte in % aus idealem Gasgesetz

                    double rel_hum = bme680.getRel_hum();
                    if(rel_hum >=80)
                        humRedList.add(location + " (" + Math.round(rel_hum) + "%&rarr;" + Math.round(relHum_Lueftung) + "%) ");
                    else if (rel_hum >=60)
                        humYellowList.add(location + " (" + Math.round(rel_hum) + "%&rarr;" + Math.round(relHum_Lueftung) + "%) ");

                }else if (sensor.contains(SENSOR_BME280)) {
                    Sensor_BME280_entity bme280 = (Sensor_BME280_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME280, location);

                    if(bme280.getId() < currentTimeInMillis-1000*60*20) //Daten älter als 20 min
                        sensorNoContactRedList.add("BME280 (" + location + ") ");
                    else if (bme280.getId() < currentTimeInMillis-1000*30) //Daten älter als 30s
                        sensorNoContactYellowList.add("BME280 (" + location + ") ");

                    double temp = bme280.getTemp();
                    double absHum = currentWeatherData.getAbs_hum();
                    double psat = 611.2*exp(17.62*temp/(243.12+temp)); //psat in Pa (Magnus-Formel Wikipedia)
                    double relHum_Lueftung = absHum*461.52*(temp+273.15)/(10*psat); //relative Feuchte in % aus idealem Gasgesetz

                    double rel_hum = bme280.getRel_hum();
                    if(rel_hum >=80)
                        humRedList.add(location + " (" + Math.round(rel_hum) + "%&rarr;" + Math.round(relHum_Lueftung) + "%) ");
                    else if (rel_hum >=60)
                        humYellowList.add(location + " (" + Math.round(rel_hum) + "%&rarr;" + Math.round(relHum_Lueftung) + "%) ");

                }else if (sensor.contains(SENSOR_PARTICLE)) {
                    Sensor_Particle_entity particle = (Sensor_Particle_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_PARTICLE, location);

                    if(particle.getId() < currentTimeInMillis-1000*60*20) //Daten älter als 20 min
                        sensorNoContactRedList.add("Partikelsensor (" + location + ") ");
                    else if (particle.getId() < currentTimeInMillis-1000*30) //Daten älter als 30s
                        sensorNoContactYellowList.add("Partikelsensor (" + location + ") ");

                    double pm10 = particle.getPm10();
                    if (pm10 >= 40){
                        particleRedList.add(location + " PM10(" + Math.round(pm10) + ") ");
                    }else if(pm10 >=20)
                        particleYellowList.add(location + " PM10(" + Math.round(pm10) + ") ");

                    double pm25 = particle.getPm25();
                    if (pm25 >= 20){
                        particleRedList.add(location + " PM2,5(" + Math.round(pm25) + ") ");
                    }else if(pm25 >=10)
                        particleYellowList.add(location + " PM2,5(" + Math.round(pm25) + ") ");
                }else
                    System.out.println("No matching entry found");
            }
        }

        if (sensorNoContactYellowList.size() > 0 && sensorNoContactRedList.size() > 0) {
            sensor_color = "opacity_alert";
            sensor_report = String.join(",", sensorNoContactYellowList) + "min. 30s keinen Kontakt " + String.join(",", sensorNoContactRedList) + "min. 20min keinen Kontakt";
        }else if  (sensorNoContactYellowList.size() > 0){
            sensor_color = "opacity_warning";
            sensor_report = String.join(",", sensorNoContactYellowList) + "min. 30s keinen Kontakt";
        }else if  (sensorNoContactRedList.size() > 0){
            sensor_color = "opacity_alert";
            sensor_report = String.join(",", sensorNoContactRedList) + "min. 20min keinen Kontakt";
        }

        if (iaqYellowList.size() > 0 && iaqRedList.size() > 0) {
            voc_color = "opacity_alert";
            voc_report = String.join(",", iaqYellowList) + "erhöht und " + String.join(",", iaqRedList) + "kritisch. Sofort Lüften!";
        }else if  (iaqYellowList.size() > 0){
            voc_color = "opacity_warning";
            voc_report = String.join(",", iaqYellowList) + "erhöht. Eventuell Lüften!";
        }else if  (iaqRedList.size() > 0){
            voc_color = "opacity_alert";
            voc_report = String.join(",", iaqRedList) + "kritisch. Sofort Lüften!";
        }

        if (humYellowList.size() > 0 && humRedList.size() > 0) {
            hum_color = "opacity_alert";
            hum_report = String.join(",", humYellowList) + "erhöht und " + String.join(",", humRedList) + "kritisch.";
        }else if  (humYellowList.size() > 0){
            hum_color = "opacity_warning";
            hum_report = String.join(",", humYellowList) + "erhöht.";
        }else if  (humRedList.size() > 0){
            hum_color = "opacity_alert";
            hum_report = String.join(",", humRedList) + "kritisch.";
        }

        if (particleYellowList.size() > 0 && particleRedList.size() > 0) {
            particle_color = "opacity_alert";
            particle_report = String.join(",", particleYellowList) + "noch unter EU Grenzwert und " + String.join(",", particleRedList) + "über Grenzwert für PM2,5 max 20&mu;g/m&sup3; und PM10 max 40&mu;g/m&sup3;";
        }else if  (particleYellowList.size() > 0){
            particle_color = "opacity_warning";
            particle_report = String.join(",", particleYellowList) + "noch unter EU Grenzwert für PM2,5 max 20&mu;g/m&sup3; und PM10 max 40&mu;g/m&sup3;";
        }else if  (particleRedList.size() > 0){
            particle_color = "opacity_alert";
            particle_report = String.join(",", particleRedList) + "über EU Grenzwert für PM2,5 max 20&mu;g/m&sup3; und PM10 max 40&mu;g/m&sup3;";
        }

        map.put("sensor_report", sensor_report);
        map.put("sensor_color", sensor_color);
        map.put("voc_report", voc_report);
        map.put("voc_color", voc_color);
        map.put("hum_report", hum_report);
        map.put("hum_color", hum_color);
        map.put("particle_report", particle_report);
        map.put("particle_color", particle_color);

        return map;
    }
}
