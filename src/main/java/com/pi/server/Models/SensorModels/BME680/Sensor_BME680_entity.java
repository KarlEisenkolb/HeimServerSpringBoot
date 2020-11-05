package com.pi.server.Models.SensorModels.BME680;

import javax.persistence.*;

@MappedSuperclass
public class Sensor_BME680_entity {

    final public static String SENSOR_BME680  = "sensor_bme680_";

    @Id
    @Column
    private long id; // is a timestamp in millis

    @Column
    private double iaq; // 0-500 (0 toll, 500 schlechteste)

    @Column
    private int iaq_accuracy; // 0-3 (0 schlecht, 3 beste)

    @Column
    private double temp; // °C

    @Column
    private double rel_hum; // relative Feuchte in %

    @Column
    private double abs_hum; // absolute Feuchte in g/m^3

    @Column
    private double press; // Druck in mbar

    @Column
    private double gasohm; // Sensorwiderstand in Ohm

    @Column
    private double co2ppm; // geschätzt CO2 in ppm

    @Column
    private double bvoceppm; // geschätzt VOC Äquivalent von Atemluft

    public Sensor_BME680_entity(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getIaq() {
        return iaq;
    }

    public void setIaq(double iaq) {
        this.iaq = iaq;
    }

    public int getIaq_accuracy() {
        return iaq_accuracy;
    }

    public void setIaq_accuracy(int iaq_accuracy) {
        this.iaq_accuracy = iaq_accuracy;
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

    public double getGasohm() {
        return gasohm;
    }

    public void setGasohm(double gasohm) {
        this.gasohm = gasohm;
    }

    public double getCo2ppm() {
        return co2ppm;
    }

    public void setCo2ppm(double co2ppm) {
        this.co2ppm = co2ppm;
    }

    public double getBvoceppm() {
        return bvoceppm;
    }

    public void setBvoceppm(double bvoceppm) {
        this.bvoceppm = bvoceppm;
    }
}
