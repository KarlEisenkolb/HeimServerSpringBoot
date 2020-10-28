package com.pi.server.HttpRequests;

import com.pi.server.Models.OpenWeather.WeatherForecast_daily_entity;
import com.pi.server.Models.OpenWeather.WeatherForecast_hourly_entity;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.exp;

public class ExtractJsonData {

    private static final String LOG_TAG = ExtractJsonData.class.getSimpleName();

    public static Weather_current_entity extractData_current(String jsonResponse) {
        Weather_current_entity weather_current = new Weather_current_entity();
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject current = jsonObj.getJSONObject("current");

            weather_current.setRequestTimestamp(System.currentTimeMillis());
            weather_current.setTime(            current.getLong("dt"));
            weather_current.setSunrise(         current.getLong("sunrise"));
            weather_current.setSunset(          current.getLong("sunset"));
            double temp = current.getDouble("temp");
            weather_current.setTemp(temp);
            weather_current.setFeels_like_temp( current.getDouble("feels_like"));
            weather_current.setPressure(        current.getDouble("pressure"));
            double relHum = current.getDouble("humidity");
            weather_current.setRel_hum(        current.getDouble("humidity"));

            double psat = 611.2*exp(17.62*temp/(243.12+temp)); //psat in Pa (Magnus-Formel Wikipedia)
            double absHum = relHum/100*psat/(461.52*(temp+273.15))*1000; //absolute Feuchte in g/m³ aus idealem Gasgesetz
            weather_current.setAbs_hum(absHum);

            weather_current.setDew_point(       current.getDouble("dew_point"));
            weather_current.setUv_index(        current.getDouble("uvi"));
            weather_current.setClouds(          current.getDouble("clouds"));
            weather_current.setVisibility(      current.getDouble("visibility"));
            weather_current.setWind_speed(      current.getDouble("wind_speed"));
            weather_current.setWind_deg(        current.getDouble("wind_deg"));

            JSONArray weather = current.getJSONArray("weather");
            JSONObject zero = weather.getJSONObject(0);

            weather_current.setWeather_id(zero.getDouble("id"));
            weather_current.setMain(zero.getString("main"));
            weather_current.setDescription(zero.getString("description"));
            weather_current.setIcon(zero.getString("icon"));

        } catch (JSONException e) {
            System.out.println(LOG_TAG + " Problem parsing JSON current results. " + e.getMessage());
        }
        return weather_current;
    }

    public static List<WeatherForecast_hourly_entity> extractAllData_hourly(String jsonResponse) {
        List<WeatherForecast_hourly_entity> listHourly = new ArrayList<>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm EEE");

        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONArray hourly = jsonObj.getJSONArray("hourly");

            for(int i=0; i<hourly.length(); i++){
                WeatherForecast_hourly_entity weather_hourly = new WeatherForecast_hourly_entity();

                JSONObject currentHour = hourly.getJSONObject(i);

                weather_hourly.setRequestTimestamp(System.currentTimeMillis());
                weather_hourly.setTime(            currentHour.getLong("dt"));
                weather_hourly.setTime_string(dateFormatter.format(currentHour.getLong("dt") * 1000));
                //weather_hourly.setSunrise(         current.getLong("sunrise"));           //not in Data hourly
                //weather_hourly.setSunset(          current.getLong("sunset"));            // not in Data hourly
                weather_hourly.setTemp(            currentHour.getDouble("temp"));
                weather_hourly.setFeels_like_temp( currentHour.getDouble("feels_like"));
                weather_hourly.setPressure(        currentHour.getDouble("pressure"));
                weather_hourly.setHumidity(        currentHour.getDouble("humidity"));
                weather_hourly.setDew_point(       currentHour.getDouble("dew_point"));
                //weather_hourly.setUv_index(        current.getDouble("uvi"));             //not in Data hourly
                weather_hourly.setClouds(          currentHour.getDouble("clouds"));
                //weather_hourly.setVisibility(      current.getDouble("visibility"));      //not in Data hourly
                weather_hourly.setWind_speed(      currentHour.getDouble("wind_speed"));
                weather_hourly.setWind_deg(        currentHour.getDouble("wind_deg"));
                weather_hourly.setPop(        currentHour.getDouble("pop") * 100);

                JSONArray weather = currentHour.getJSONArray("weather");
                JSONObject zero = weather.getJSONObject(0);

                weather_hourly.setWeather_id(zero.getDouble("id"));
                weather_hourly.setMain(zero.getString("main"));
                weather_hourly.setDescription(zero.getString("description"));
                weather_hourly.setIcon(zero.getString("icon"));

                listHourly.add(weather_hourly);
            }
        } catch (JSONException e) {
            System.out.println(LOG_TAG + " Problem parsing JSON hourly results. " + e.getMessage());
        }
        return listHourly;
    }

    public static List<WeatherForecast_daily_entity> extractAllData_daily(String jsonResponse) {
        List<WeatherForecast_daily_entity> listDaily = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONArray daily = jsonObj.getJSONArray("daily");

            for(int i=0; i<daily.length(); i++){
                WeatherForecast_daily_entity weather_daily = new WeatherForecast_daily_entity();

                JSONObject currentDay = daily.getJSONObject(i);

                weather_daily.setRequestTimestamp(System.currentTimeMillis());
                weather_daily.setTime(            currentDay.getLong("dt"));
                weather_daily.setSunrise(         currentDay.getLong("sunrise"));
                weather_daily.setSunset(          currentDay.getLong("sunset"));
                //weather_daily.setTemp(            currentDay.getDouble("temp"));
                //weather_daily.setFeels_like_temp( currentDay.getDouble("feels_like"));

                JSONObject currentTemps = currentDay.getJSONObject("temp");
                weather_daily.setTemp_day(            currentTemps.getDouble("day"));
                weather_daily.setTemp_min(            currentTemps.getDouble("min"));
                weather_daily.setTemp_max(            currentTemps.getDouble("max"));
                weather_daily.setTemp_night(          currentTemps.getDouble("night"));
                weather_daily.setTemp_eve(            currentTemps.getDouble("eve"));
                weather_daily.setTemp_morn(           currentTemps.getDouble("morn"));

                JSONObject currentTempsFeelLike = currentDay.getJSONObject("feels_like");
                weather_daily.setFeels_like_temp_day(       currentTempsFeelLike.getDouble("day"));
                weather_daily.setFeels_like_temp_night(     currentTempsFeelLike.getDouble("night"));
                weather_daily.setFeels_like_temp_eve(       currentTempsFeelLike.getDouble("eve"));
                weather_daily.setFeels_like_temp_morn(      currentTempsFeelLike.getDouble("morn"));

                weather_daily.setPressure(        currentDay.getDouble("pressure"));
                weather_daily.setHumidity(        currentDay.getDouble("humidity"));
                weather_daily.setDew_point(       currentDay.getDouble("dew_point"));
                weather_daily.setUv_index(        currentDay.getDouble("uvi"));
                weather_daily.setClouds(          currentDay.getDouble("clouds"));
                //weather_daily.setVisibility(      currentDay.getDouble("visibility"));
                weather_daily.setWind_speed(      currentDay.getDouble("wind_speed"));
                weather_daily.setWind_deg(        currentDay.getDouble("wind_deg"));
                weather_daily.setPop(Math.round(currentDay.getDouble("pop") * 100 * 100.0) / 100.0);

                JSONArray weather = currentDay.getJSONArray("weather");
                JSONObject zero = weather.getJSONObject(0);

                weather_daily.setWeather_id(zero.getDouble("id"));
                weather_daily.setMain(zero.getString("main"));
                weather_daily.setDescription(zero.getString("description"));
                weather_daily.setIcon(zero.getString("icon"));

                listDaily.add(weather_daily);
            }
        } catch (JSONException e) {
            System.out.println(LOG_TAG + " Problem parsing JSON daily results. " + e.getMessage());
        }
        return listDaily;
    }
}
