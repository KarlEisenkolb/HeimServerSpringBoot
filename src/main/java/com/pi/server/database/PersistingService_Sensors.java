package com.pi.server.database;

import com.pi.server.database.sensors.DAO_Sensor;
import com.pi.server.gui_controller_out.MainController;
import com.pi.server.models.sensor_models.bme280.Sensor_BME280_entity_Bad;
import com.pi.server.models.sensor_models.bme680.Sensor_BME680_entity_Bibliothek;
import com.pi.server.models.sensor_models.bme680.Sensor_BME680_entity_Schlafzimmer;
import com.pi.server.models.sensor_models.particle_adafruit.Sensor_Particle_Plantower_entity_Bibliothek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pi.server.models.sensor_models.bme280.Sensor_BME280_entity.SENSOR_BME280;
import static com.pi.server.models.sensor_models.bme680.Sensor_BME680_entity.SENSOR_BME680;
import static com.pi.server.models.sensor_models.particle_adafruit.Sensor_Particle_Plantower_entity.SENSOR_PARTICLE_PLANTOWER;

@Component
public class PersistingService_Sensors {

    private final Logger log = LoggerFactory.getLogger(PersistingService_Sensors.class);

    final public static String LOCATION_BIBLIOTHEK    = "bibliothek";
    final public static String LOCATION_BAD           = "bad";
    final public static String LOCATION_SCHLAFZIMMER  = "schlafzimmer";

    final public static Map<String, List<String>> sensorLocations = new HashMap<>();

    public PersistingService_Sensors(){
        List<String> sensorList_Bibliothek = new ArrayList<>();
        sensorList_Bibliothek.add(Sensor_BME680_entity_Bibliothek.TableName);
        sensorList_Bibliothek.add(Sensor_Particle_Plantower_entity_Bibliothek.TableName);
        sensorLocations.put(LOCATION_BIBLIOTHEK, sensorList_Bibliothek);

        List<String> sensorList_Bad = new ArrayList<>();
        sensorList_Bad.add(Sensor_BME280_entity_Bad.TableName);
        sensorLocations.put(LOCATION_BAD, sensorList_Bad);

        List<String> sensorList_Schlafzimmer = new ArrayList<>();
        sensorList_Schlafzimmer.add(Sensor_BME680_entity_Schlafzimmer.TableName);
        sensorLocations.put(LOCATION_SCHLAFZIMMER, sensorList_Schlafzimmer);
    }

    @Autowired
    @Qualifier("DAO_MariaDB_BME680_Impl")
    private DAO_Sensor dao_bme680;

    @Autowired
    @Qualifier("DAO_MariaDB_BME280_Impl")
    private DAO_Sensor dao_bme280;

    @Autowired
    @Qualifier("DAO_MariaDB_Particle_Plantower_Impl")
    private DAO_Sensor dao_particle;
    public final static int particle_data = 2;

    public Object getLastItem_withTableName(String requestedSensor, String location){
        if (requestedSensor.equals(SENSOR_BME280))
            return dao_bme280.getLastItem_withTableName(SENSOR_BME280 + location);
        else if (requestedSensor.equals(SENSOR_BME680))
            return dao_bme680.getLastItem_withTableName(SENSOR_BME680 + location);
        else if (requestedSensor.equals(SENSOR_PARTICLE_PLANTOWER))
            return dao_particle.getLastItem_withTableName(SENSOR_PARTICLE_PLANTOWER + location);
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }

    public List<Object> getAll_withStartAndEndTime_withTableName(long startTime, long endTime, String requestedSensor, String location){
        if (requestedSensor.equals(SENSOR_BME280))
            return dao_bme280.getAll_withStartAndEndTime_withTableName(startTime, endTime, SENSOR_BME280 + location);
        else if (requestedSensor.equals(SENSOR_BME680))
            return dao_bme680.getAll_withStartAndEndTime_withTableName(startTime, endTime, SENSOR_BME680 + location);
        else if (requestedSensor.equals(SENSOR_PARTICLE_PLANTOWER))
            return dao_particle.getAll_withStartAndEndTime_withTableName(startTime, endTime, SENSOR_PARTICLE_PLANTOWER + location);
        else{
            log.warn(" No known Object to get from Database requested");
            return null;
        }
    }
}
