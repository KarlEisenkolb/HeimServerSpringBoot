package com.pi.server.models.sensor_models.particle_novafitness;

import javax.persistence.*;

@MappedSuperclass
public class Sensor_Particle_NovaFitness_entity {

    final public static String SENSOR_PARTICLE_NOVAFITNESS  = "sensor_particle_novafitness_";

    @Id
    @Column
    private long id; // is a timestamp in millis

    @Column
    private double pm25; // actually PM2,5 not PM25 !

    @Column
    private double pm10; // PM10

    public Sensor_Particle_NovaFitness_entity(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }
}
