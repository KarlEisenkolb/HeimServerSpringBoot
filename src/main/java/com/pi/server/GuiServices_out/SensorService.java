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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Object getLatestSensorDataForGuiUpdate(String currReqLocation) {
        long currentTimeInMillis = System.currentTimeMillis();
        Weather_current_entity currentWeatherData   = (Weather_current_entity) persistingService_weather.getLastItem(CurrentWeather);
        HashMap<String, Object> map = new HashMap<>();
        map.put("latest_plot_aussentemp", new Mav_XYPlotData(currentWeatherData.getRequest_timestamp(), currentWeatherData.getTemp()));

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
                    putDataIntoMapForFrontend_BME680(currReqLocation, map, location, bme680);
                    sensorStatus(currentTimeInMillis, sensorNoContactYellowList, sensorNoContactRedList, location, bme680.getId(), "BME680 (");
                    iaqStatus(iaqYellowList, iaqRedList, location, bme680.getIaq());
                    humidityStatus(currentWeatherData, humYellowList, humRedList, location, bme680.getTemp(), bme680.getRel_hum());

                }else if (sensor.contains(SENSOR_BME280)) {
                    Sensor_BME280_entity bme280 = (Sensor_BME280_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_BME280, location);
                    putDataIntoMapForFrontend_BME280(currReqLocation, map, location, bme280);
                    sensorStatus(currentTimeInMillis, sensorNoContactYellowList, sensorNoContactRedList, location, bme280.getId(), "BME280 (");
                    humidityStatus(currentWeatherData, humYellowList, humRedList, location, bme280.getTemp(), bme280.getRel_hum());

                }else if (sensor.contains(SENSOR_PARTICLE)) {
                    Sensor_Particle_entity particle = (Sensor_Particle_entity) persistingService_sensors.getLastItem_withTableName(SENSOR_PARTICLE, location);
                    putDataIntoMapForFrontend_Particle(currReqLocation, map, location, particle);
                    sensorStatus(currentTimeInMillis, sensorNoContactYellowList, sensorNoContactRedList, location, particle.getId(), "Partikelsensor (");
                    particleStatus(particleYellowList, particleRedList, location, particle);

                }else
                    System.out.println("No matching entry found");
            }
        }

        sensor_color = settingStatusColors(sensorNoContactYellowList, sensorNoContactRedList, sensor_color);
        voc_color = settingStatusColors(iaqYellowList, iaqRedList, voc_color);
        hum_color = settingStatusColors(humYellowList, humRedList, hum_color);
        particle_color = settingStatusColors(particleYellowList, particleRedList, particle_color);

        sensor_report = settingStatusReport(sensor_report, sensorNoContactYellowList, sensorNoContactRedList, "min. 30s keinen Kontakt ", "min. 20min keinen Kontakt", "min. 30s keinen Kontakt");
        voc_report = settingStatusReport(voc_report, iaqYellowList, iaqRedList, "erhöht und ", "kritisch. Sofort Lüften!", "erhöht. Eventuell Lüften!");
        hum_report = settingStatusReport(hum_report, humYellowList, humRedList, "erhöht und ", "kritisch.", "erhöht.");
        particle_report = settingStatusReport(particle_report, particleYellowList, particleRedList, "noch unter EU Grenzwert und ", "über Grenzwert für PM2,5 max 20&mu;g/m&sup3; und PM10 max 40&mu;g/m&sup3;", "noch unter EU Grenzwert für PM2,5 max 20&mu;g/m&sup3; und PM10 max 40&mu;g/m&sup3;");

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

    private String settingStatusReport(String sensor_report, List<String> sensorNoContactYellowList, List<String> sensorNoContactRedList, String yellowStringBeforeRed, String redString, String onlyYellowString) {
        if (sensorNoContactYellowList.size() > 0 && sensorNoContactRedList.size() > 0) {
            sensor_report = String.join(",", sensorNoContactYellowList) + yellowStringBeforeRed + String.join(",", sensorNoContactRedList) + redString;
        } else if (sensorNoContactYellowList.size() > 0) {
            sensor_report = String.join(",", sensorNoContactYellowList) + onlyYellowString;
        } else if (sensorNoContactRedList.size() > 0) {
            sensor_report = String.join(",", sensorNoContactRedList) + redString;
        }
        return sensor_report;
    }

    private String settingStatusColors(List<String> yellowList, List<String> redList, String color) {
        if (redList.size() > 0) {
            color = "opacity_alert";
        }else if  (yellowList.size() > 0){
            color = "opacity_warning";
        }
        return color;
    }

    private void iaqStatus(List<String> iaqYellowList, List<String> iaqRedList, String location, double iaq) {
        if(iaq >=200)
            iaqRedList.add(location + " (" + Math.round(iaq) + ") ");
        else if (iaq >= 150)
            iaqYellowList.add(location + " (" + Math.round(iaq) + ") ");
    }

    private void particleStatus(List<String> particleYellowList, List<String> particleRedList, String location, Sensor_Particle_entity particle) {
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
    }

    private void humidityStatus(Weather_current_entity currentWeatherData, List<String> humYellowList, List<String> humRedList, String location, double temp, double rel_hum) {
        double absHum = currentWeatherData.getAbs_hum();
        double psat = 611.2*exp(17.62*temp/(243.12+temp)); //psat in Pa (Magnus-Formel Wikipedia)
        double relHum_Lueftung = absHum*461.52*(temp+273.15)/(10*psat); //relative Feuchte in % aus idealem Gasgesetz

        if(rel_hum >=80)
            humRedList.add(location + " (" + Math.round(rel_hum) + "%&rarr;" + Math.round(relHum_Lueftung) + "%) ");
        else if (rel_hum >=60)
            humYellowList.add(location + " (" + Math.round(rel_hum) + "%&rarr;" + Math.round(relHum_Lueftung) + "%) ");
    }

    private void sensorStatus(long currentTimeInMillis, List<String> sensorNoContactYellowList, List<String> sensorNoContactRedList, String location, long id_timestamp, String sensorName) {
        if (id_timestamp < currentTimeInMillis - 1000 * 60 * 20) //Daten älter als 20 min
            sensorNoContactRedList.add(sensorName + location + ") ");
        else if (id_timestamp < currentTimeInMillis - 1000 * 30) //Daten älter als 30s
            sensorNoContactYellowList.add(sensorName + location + ") ");
    }

    private void putDataIntoMapForFrontend_Particle(String currReqLocation, HashMap<String, Object> map, String location, Sensor_Particle_entity particle) {
        map.put("latest_pm25_" + location, new Mav_XYPlotData(particle.getId(), particle.getPm25()));
        map.put("latest_pm10_" + location, new Mav_XYPlotData(particle.getId(), particle.getPm10()));

        if (location.equals(currReqLocation)){
            map.put("latest_plot_pm25", new Mav_XYPlotData(particle.getId(), particle.getPm25()));
            map.put("latest_plot_pm10", new Mav_XYPlotData(particle.getId(), particle.getPm10()));
        }
    }

    private void putDataIntoMapForFrontend_BME280(String currReqLocation, HashMap<String, Object> map, String location, Sensor_BME280_entity bme280) {
        map.put("latest_temp_" + location, new Mav_XYPlotData(bme280.getId(), bme280.getTemp()));
        map.put("latest_relhum_" + location, new Mav_XYPlotData(bme280.getId(), bme280.getRel_hum()));
        map.put("latest_abshum_" + location, new Mav_XYPlotData(bme280.getId(), bme280.getAbs_hum()));

        if (location.equals(currReqLocation)){
            map.put("latest_plot_iaq", new Mav_XYPlotData(bme280.getId(), 0));
            map.put("latest_plot_temp", new Mav_XYPlotData(bme280.getId(), bme280.getTemp()));
            map.put("latest_plot_relhum", new Mav_XYPlotData(bme280.getId(), bme280.getRel_hum()));
        }
    }

    private void putDataIntoMapForFrontend_BME680(String currReqLocation, HashMap<String, Object> map, String location, Sensor_BME680_entity bme680) {
        map.put("latest_iaq_" + location, new Mav_XYPlotData(bme680.getId(), bme680.getIaq()));
        map.put("latest_temp_" + location, new Mav_XYPlotData(bme680.getId(), bme680.getTemp()));
        map.put("latest_relhum_" + location, new Mav_XYPlotData(bme680.getId(), bme680.getRel_hum()));
        map.put("latest_abshum_" + location, new Mav_XYPlotData(bme680.getId(), bme680.getAbs_hum()));

        if (location.equals(currReqLocation)){
            map.put("latest_plot_iaq", new Mav_XYPlotData(bme680.getId(), bme680.getIaq()));
            map.put("latest_plot_temp", new Mav_XYPlotData(bme680.getId(), bme680.getTemp()));
            map.put("latest_plot_relhum", new Mav_XYPlotData(bme680.getId(), bme680.getRel_hum()));
        }
    }
}
