package com.pi.server.models.sensor_models.particle_adafruit;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Sensor_Particle_Plantower_entity {

    final public static String SENSOR_PARTICLE_PLANTOWER  = "sensor_particle_plantower_";

    @Id
    @Column
    private long id; // is a timestamp in millis

    @Column
    private double pm1s; // PM1  standard environment

    @Column
    private double pm25s; // actually PM2,5 not PM25 !  standard environment

    @Column
    private double pm10s; // PM10  standard environment

    @Column
    private double pm1e; // PM1 atmospheric environment

    @Column
    private double pm25e; // actually PM2,5 not PM25 ! atmospheric environment

    @Column
    private double pm10e; // PM10 atmospheric environment

    @Column
    private double p03; // Anzahl Partikel/100ml groesser als 0.3mu standard environment

    @Column
    private double p05; // Anzahl Partikel/100ml groesser als 0.5mu standard environment

    @Column
    private double p1; // Anzahl Partikel/100ml groesser als 1mu standard environment

    @Column
    private double p25; // Anzahl Partikel/100ml groesser als 2.5mu standard environment

    @Column
    private double p5; // Anzahl Partikel/100ml groesser als 5mu standard environment

    @Column
    private double p10; // Anzahl Partikel/100ml groesser als 10mu standard environment

    public Sensor_Particle_Plantower_entity(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPm1s() {
        return pm1s;
    }

    public void setPm1s(double pm1s) {
        this.pm1s = pm1s;
    }

    public double getPm25s() {
        return pm25s;
    }

    public void setPm25s(double pm25s) {
        this.pm25s = pm25s;
    }

    public double getPm10s() {
        return pm10s;
    }

    public void setPm10s(double pm10s) {
        this.pm10s = pm10s;
    }

    public double getPm1e() {
        return pm1e;
    }

    public void setPm1e(double pm1e) {
        this.pm1e = pm1e;
    }

    public double getPm25e() {
        return pm25e;
    }

    public void setPm25e(double pm25e) {
        this.pm25e = pm25e;
    }

    public double getPm10e() {
        return pm10e;
    }

    public void setPm10e(double pm10e) {
        this.pm10e = pm10e;
    }

    public double getP03() {
        return p03;
    }

    public void setP03(double p03) {
        this.p03 = p03;
    }

    public double getP05() {
        return p05;
    }

    public void setP05(double p05) {
        this.p05 = p05;
    }

    public double getP1() {
        return p1;
    }

    public void setP1(double p1) {
        this.p1 = p1;
    }

    public double getP25() {
        return p25;
    }

    public void setP25(double p25) {
        this.p25 = p25;
    }

    public double getP5() {
        return p5;
    }

    public void setP5(double p5) {
        this.p5 = p5;
    }

    public double getP10() {
        return p10;
    }

    public void setP10(double p10) {
        this.p10 = p10;
    }
}
