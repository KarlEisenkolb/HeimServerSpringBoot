package com.pi.server.Models.SensorModels;

import javax.persistence.*;

@Entity(name = Sensor_Particle_entity.TableName)
@Table(name = Sensor_Particle_entity.TableName)
public class Sensor_Particle_entity {

    public final static String TableName = "sensor_particle";

    @Id
    @Column
    private long id; // is a timestamp in millis

    @Column
    private double pm25; // actually PM2,5 not PM25 !

    @Column
    private double pm10; // PM10

    public Sensor_Particle_entity(){}

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
