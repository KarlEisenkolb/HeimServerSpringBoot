package com.pi.server.models.open_weather;

import javax.persistence.*;

@Entity(name = WeatherForecast_hourly_entity.TableName)
@Table(name = WeatherForecast_hourly_entity.TableName)
public class WeatherForecast_hourly_entity {

    public final static String TableName = "weather_forecast_hourly";

    //Felder:
    //sunrise,sunset,uvi,visibility
    //werden nicht gesetzt oder abgefragt da in API-Anfrage nicht vorhanden

    @Id
    @GeneratedValue
    private long id;

    @Column
    private long request_timestamp; //timestamp des Servers bei Api-Abfrage und Einspeicherung in Datenbank

    @Column
    private long time;

    @Column
    private String time_string;

    @Column
    private long sunrise;

    @Column
    private long sunset;

    @Column
    private double temp;

    @Column
    private double feels_like_temp;

    @Column
    private double pressure;

    @Column
    private double humidity;

    @Column
    private double dew_point;

    @Column
    private double uv_index;

    @Column
    private double clouds;

    @Column
    private double visibility;

    @Column
    private double wind_speed;

    @Column
    private double wind_deg;

    @Column
    private double weather_id;

    @Column
    private String main;

    @Column
    private String description;

    @Column
    private String icon;

    @Column
    private double pop;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequestTimestamp() {
        return request_timestamp;
    }

    public void setRequestTimestamp(long request_timestamp) {
        this.request_timestamp = request_timestamp;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeels_like_temp() {
        return feels_like_temp;
    }

    public void setFeels_like_temp(double feels_like_temp) {
        this.feels_like_temp = feels_like_temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getDew_point() {
        return dew_point;
    }

    public void setDew_point(double dew_point) {
        this.dew_point = dew_point;
    }

    public double getUv_index() {
        return uv_index;
    }

    public void setUv_index(double uv_index) {
        this.uv_index = uv_index;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public double getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(double wind_deg) {
        this.wind_deg = wind_deg;
    }

    public double getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(double weather_id) {
        this.weather_id = weather_id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime_string() {
        return time_string;
    }

    public void setTime_string(String time_string) {
        this.time_string = time_string;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }
}