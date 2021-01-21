package com.pi.server.models.sensor_models;

public class Mav_XYPlotData {

    private long x;
    private double y;

    public Mav_XYPlotData() {
    }

    public Mav_XYPlotData(long x, double y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
