package com.pi.server.GuiServices_out;

import com.pi.server.DatabaseManagment.PersistingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
