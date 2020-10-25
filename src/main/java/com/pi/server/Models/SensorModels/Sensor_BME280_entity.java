package com.pi.server.Models.SensorModels;
import javax.persistence.*;

@Entity(name = Sensor_BME280_entity.TableName)
@Table(name = Sensor_BME280_entity.TableName)
public class Sensor_BME280_entity {

    public final static String TableName = "sensor_bme280";

    @Id
    @Column
    private long id; // is a timestamp in millis

    @Column
    private double temp; // °C

    @Column
    private double rel_hum; // relative Feuchte in %

    @Column
    private double abs_hum; // absolute Feuchte in g/m^3

    @Column
    private double press; // Druck in mbar
}
