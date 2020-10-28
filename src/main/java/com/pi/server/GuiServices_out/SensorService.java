package com.pi.server.GuiServices_out;

import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.SensorModels.Mav_XYPlotData;
import com.pi.server.Models.SensorModels.Sensor_BME280_entity;
import com.pi.server.Models.SensorModels.Sensor_BME680_entity;
import com.pi.server.Models.SensorModels.Sensor_Particle_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SensorService {

    @Autowired
    private PersistingService persistingService;

    long timeIntervall = 36*60*60*1000; // 36h in millis

    public SensorService(){}

    public List getBme680Content(){
        long currentTimeInMillis = System.currentTimeMillis();
        return persistingService.getAllInTimeframe(PersistingService.bme680_data, currentTimeInMillis-timeIntervall, currentTimeInMillis);
    }

    public List getParticleContent(){
        long currentTimeInMillis = System.currentTimeMillis();
        return persistingService.getAllInTimeframe(PersistingService.particle_data, currentTimeInMillis-timeIntervall, currentTimeInMillis);
    }

    public List getCurrentWeatherData(){
        long currentTimeInMillis = System.currentTimeMillis();
        return persistingService.getAllInTimeframe(PersistingService.CurrentWeather, currentTimeInMillis-timeIntervall, currentTimeInMillis);
    }

    public Object getLatestSensorDataForGuiUpdate() {
        List<Mav_XYPlotData> newXYPlotList = new ArrayList<>();
        Sensor_BME280_entity bme280Data             = (Sensor_BME280_entity) persistingService.getLastItem(PersistingService.bme280_data);
        Sensor_BME680_entity bme680Data             = (Sensor_BME680_entity) persistingService.getLastItem(PersistingService.bme680_data);
        Sensor_Particle_entity particleData         = (Sensor_Particle_entity) persistingService.getLastItem(PersistingService.particle_data);
        Weather_current_entity currentWeatherData   = (Weather_current_entity) persistingService.getLastItem(PersistingService.CurrentWeather);

        HashMap<String, Object> map = new HashMap<>();

        newXYPlotList.add(new Mav_XYPlotData(particleData.getId(), particleData.getPm25())); // 0
        newXYPlotList.add(new Mav_XYPlotData(particleData.getId(), particleData.getPm10())); // 1
        newXYPlotList.add(new Mav_XYPlotData(bme680Data.getId(), bme680Data.getIaq())); // 2

        newXYPlotList.add(new Mav_XYPlotData(bme680Data.getId(), bme680Data.getTemp())); // 3
        newXYPlotList.add(new Mav_XYPlotData(bme680Data.getId(), bme680Data.getRel_hum())); // 4
        newXYPlotList.add(new Mav_XYPlotData(currentWeatherData.getRequestTimestamp(), currentWeatherData.getTemp())); // 5

        //zusätzliche Daten für die Tabelle
        newXYPlotList.add(new Mav_XYPlotData(bme680Data.getId(), bme680Data.getAbs_hum())); // 6
        newXYPlotList.add(new Mav_XYPlotData(bme280Data.getId(), bme280Data.getTemp())); // 7
        newXYPlotList.add(new Mav_XYPlotData(bme280Data.getId(), bme280Data.getRel_hum())); // 8
        newXYPlotList.add(new Mav_XYPlotData(bme280Data.getId(), bme280Data.getAbs_hum())); // 9

        return newXYPlotList;
    }
}
