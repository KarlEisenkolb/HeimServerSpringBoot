package com.pi.server.models.sensor_models.particle_adafruit;

import com.pi.server.database.PersistingService_Sensors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = Sensor_Particle_Plantower_entity_Bibliothek.TableName)
@Table(name = Sensor_Particle_Plantower_entity_Bibliothek.TableName)
public class Sensor_Particle_Plantower_entity_Bibliothek extends Sensor_Particle_Plantower_entity {

    public final static String TableName = SENSOR_PARTICLE_PLANTOWER + PersistingService_Sensors.LOCATION_BIBLIOTHEK;

}
