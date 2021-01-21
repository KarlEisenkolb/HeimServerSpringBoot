package com.pi.server.models.sensor_models.bme280;

import javax.persistence.*;

@MappedSuperclass
public class Sensor_BME280_entity {

    final public static String SENSOR_BME280  = "sensor_bme280_";

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getRel_hum() {
        return rel_hum;
    }

    public void setRel_hum(double rel_hum) {
        this.rel_hum = rel_hum;
    }

    public double getAbs_hum() {
        return abs_hum;
    }

    public void setAbs_hum(double abs_hum) {
        this.abs_hum = abs_hum;
    }

    public double getPress() {
        return press;
    }

    public void setPress(double press) {
        this.press = press;
    }
}
