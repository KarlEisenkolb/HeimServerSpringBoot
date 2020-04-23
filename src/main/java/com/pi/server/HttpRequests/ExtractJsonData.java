package com.pi.server.HttpRequests;

import com.pi.server.Models.Weather_current;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExtractJsonData {

    private static final String LOG_TAG = ExtractJsonData.class.getSimpleName();

    public static Weather_current extractData_current(String jsonResponse) {
        Weather_current weather_current = new Weather_current();
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject current = jsonObj.getJSONObject("current");

            weather_current.setRequestTimestamp(System.currentTimeMillis());
            weather_current.setTime(            current.getLong("dt"));
            weather_current.setSunrise(         current.getLong("sunrise"));
            weather_current.setSunset(          current.getLong("sunset"));
            weather_current.setTemp(            current.getDouble("temp"));
            weather_current.setFeels_like_temp( current.getDouble("feels_like"));
            weather_current.setPressure(        current.getDouble("pressure"));
            weather_current.setHumidity(        current.getDouble("humidity"));
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

    public static double extractAllData_hourly(String jsonResponse) {
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject weather = jsonObj.getJSONObject("current");
        } catch (JSONException e) {
            System.out.println(LOG_TAG + " Problem parsing JSON hourly results. " + e.getMessage());
        }
        return 2;
    }

    public static double extractAllData_daily(String jsonResponse) {
        try {
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONObject weather = jsonObj.getJSONObject("current");
        } catch (JSONException e) {
            System.out.println(LOG_TAG + " Problem parsing JSON daily results. " + e.getMessage());
        }
        return 2;
    }
}
