package com.pi.server.GuiServices_out;

import com.pi.server.DatabaseManagment.PersistingService;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.SensorModels.Mav_XYPlotData;
import com.pi.server.Models.SensorModels.Sensor_BME680_entity;
import com.pi.server.Models.SensorModels.Sensor_Particle_entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Sensor_BME680_entity bme680Data             = (Sensor_BME680_entity) persistingService.getLastItem(PersistingService.bme680_data);
        Sensor_Particle_entity particleData         = (Sensor_Particle_entity) persistingService.getLastItem(PersistingService.particle_data);
        Weather_current_entity currentWeatherData   = (Weather_current_entity) persistingService.getLastItem(PersistingService.CurrentWeather);

        newXYPlotList.add(new Mav_XYPlotData(particleData.getId(), particleData.getPm25()));
        newXYPlotList.add(new Mav_XYPlotData(particleData.getId(), particleData.getPm10()));
        newXYPlotList.add(new Mav_XYPlotData(bme680Data.getId(), bme680Data.getIaq()));

        newXYPlotList.add(new Mav_XYPlotData(bme680Data.getId(), bme680Data.getTemp()));
        newXYPlotList.add(new Mav_XYPlotData(bme680Data.getId(), bme680Data.getRel_hum()));
        newXYPlotList.add(new Mav_XYPlotData(currentWeatherData.getRequestTimestamp(), currentWeatherData.getTemp()));

        return newXYPlotList;
    }
}
