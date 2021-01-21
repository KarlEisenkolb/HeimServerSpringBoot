package com.pi.server.models.sensor_models.bme280;

import com.pi.server.database.PersistingService_Sensors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = Sensor_BME280_entity_Bad.TableName)
@Table(name = Sensor_BME280_entity_Bad.TableName)
public class Sensor_BME280_entity_Bad extends Sensor_BME280_entity {

    public final static String TableName = SENSOR_BME280 + PersistingService_Sensors.LOCATION_BAD;

}
