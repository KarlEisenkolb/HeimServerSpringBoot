package com.pi.server.Models;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;

@Entity
@Table(name = "weather_forecast_daily")
public class WeatherForecast_daily {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name = "fischkopp3";
}