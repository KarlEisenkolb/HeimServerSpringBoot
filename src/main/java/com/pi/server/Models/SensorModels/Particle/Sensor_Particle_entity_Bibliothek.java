package com.pi.server.Models.SensorModels.Particle;

import com.pi.server.DatabaseManagment.PersistingService_Sensors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = Sensor_Particle_entity_Bibliothek.TableName)
@Table(name = Sensor_Particle_entity_Bibliothek.TableName)
public class Sensor_Particle_entity_Bibliothek extends Sensor_Particle_entity {

    public final static String TableName = SENSOR_PARTICLE + PersistingService_Sensors.LOCATION_BIBLIOTHEK;

}
