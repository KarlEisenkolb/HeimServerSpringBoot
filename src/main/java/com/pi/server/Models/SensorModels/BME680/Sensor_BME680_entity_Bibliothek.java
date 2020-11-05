package com.pi.server.Models.SensorModels.BME680;

import com.pi.server.DatabaseManagment.PersistingService_Sensors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = Sensor_BME680_entity_Bibliothek.TableName)
@Table(name = Sensor_BME680_entity_Bibliothek.TableName)
public class Sensor_BME680_entity_Bibliothek extends Sensor_BME680_entity {

    public final static String TableName = SENSOR_BME680 + PersistingService_Sensors.LOCATION_BIBLIOTHEK;

}
