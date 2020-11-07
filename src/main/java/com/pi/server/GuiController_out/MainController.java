package com.pi.server.GuiController_out;

import com.pi.server.GuiServices_out.MainService;
import com.pi.server.GuiServices_out.SensorService;
import com.pi.server.GuiServices_out.ServerDataStatusService;
import com.pi.server.Models.OpenWeather.Weather_current_entity;
import com.pi.server.Models.SensorModels.Mav_XYPlotData;
import com.pi.server.Models.SensorModels.BME680.Sensor_BME680_entity;
import com.pi.server.Models.SensorModels.Particle.Sensor_Particle_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MainService mainService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private ServerDataStatusService serverDataStatusService;

    private final int anzahl_Tageskacheln = 7; // anzahl an weiteren tagen nach Heute
    private final Logger log = LoggerFactory.getLogger(MainController.class);
    private final String TEMPLATE_MAIN = "main";

    @Value("${server.port}")
    private int serverPort;
    @Value("${server.ip}")
    private String serverIp;
    private String WEBSITE_GET_DATA_STATUS_URL;
    private String WEBSITE_UPDATE_PLOT_DATA_URL;

    @EventListener(ApplicationReadyEvent.class)
    public void getServerDataUrlAfterServerStart(){
        WEBSITE_GET_DATA_STATUS_URL = "http://" + serverIp + ":" + serverPort + "/data_status";
        WEBSITE_UPDATE_PLOT_DATA_URL = "http://" + serverIp + ":" + serverPort + "/update_plot_data";
        //WEBSITE_UPDATE_PLOT_DATA_URL = "http://localhost:1990/update_plot_data";
        System.out.println(WEBSITE_GET_DATA_STATUS_URL);
    }

    @RequestMapping("/")
    public ModelAndView mainTemplate(@RequestParam String location){
        ModelAndView mav = new ModelAndView(TEMPLATE_MAIN);

        basicData(mav, location);
        sensorData(mav, location);
        weatherData(mav);
        organisationsappData(mav);

        return mav;
    }

    @RequestMapping("/data_status")
    public String checkForNewData(){
        return serverDataStatusService.getServerStatus();
    }

    @RequestMapping("/update_plot_data")
    public Object updatePlotData(@RequestParam String location){
        return sensorService.getLatestSensorDataForGuiUpdate(location);
    }

    private void basicData(ModelAndView mav, String location) {
        WEBSITE_UPDATE_PLOT_DATA_URL += "/?location=" + location;
        mav.addObject("website_get_data_status_url", WEBSITE_GET_DATA_STATUS_URL);
        mav.addObject("website_update_plot_data_url", WEBSITE_UPDATE_PLOT_DATA_URL);
        mav.addObject("time_and_date_string_long", mainService.getTimeAndDateString(mainService.DATE_LONG));
        mav.addObject("time_and_date_string_short", mainService.getTimeAndDateString(mainService.DATE_SHORT));
    }

    private void organisationsappData(ModelAndView mav) {
        List dayterminlist = mainService.getTermineForDaysToDisplay(anzahl_Tageskacheln);
        mav.addObject("currentdayterminlist", dayterminlist.get(0));
        dayterminlist.remove(0);
        mav.addObject("dayterminlist", dayterminlist);
    }

    private void weatherData(ModelAndView mav) {
        mav.addObject("latestWeather", mainService.getLatestCurrentWeather());

        List weatherHourlyList = mainService.getWeatherHourlyForecastContent();
        mav.addObject("hourlyweatherlist", weatherHourlyList);

        List weatherDailyList = mainService.getWeatherDailyForecastContent();
        weatherDailyList.remove(0);
        mav.addObject("dailyweatherlist", weatherDailyList);
        mav.addObject("headingsdates", mainService.getDateHeadingStrings(anzahl_Tageskacheln));
    }

    private void sensorData(ModelAndView mav, String location){

        List bme680List = sensorService.getBme680Content(location);
        List<Mav_XYPlotData> iaq        = new ArrayList<>();
        List<Mav_XYPlotData> temp_in    = new ArrayList<>();
        List<Mav_XYPlotData> rel_hum    = new ArrayList<>();

        for(Sensor_BME680_entity bme680 : (List<Sensor_BME680_entity>)(List<?>) bme680List){
            iaq.add(    new Mav_XYPlotData(bme680.getId(), bme680.getIaq()));
            temp_in.add(new Mav_XYPlotData(bme680.getId(), bme680.getTemp()));
            rel_hum.add(new Mav_XYPlotData(bme680.getId(), bme680.getRel_hum()));
        }
        mav.addObject("iaq", iaq);
        mav.addObject("temp_in", temp_in);
        mav.addObject("rel_hum", rel_hum);


        List particleList = sensorService.getParticleContent(location);
        List<Mav_XYPlotData> pm25   = new ArrayList<>();
        List<Mav_XYPlotData> pm10   = new ArrayList<>();

        for(Sensor_Particle_entity particle : (List<Sensor_Particle_entity>)(List<?>) particleList){
            pm25.add(new Mav_XYPlotData(particle.getId(), particle.getPm25()));
            pm10.add(new Mav_XYPlotData(particle.getId(), particle.getPm10()));
        }
        mav.addObject("pm25", pm25);
        mav.addObject("pm10", pm10);

        List currentWeatherDataList = sensorService.getCurrentWeatherData();
        List<Mav_XYPlotData> currentweather_templist   = new ArrayList<>();

        for(Weather_current_entity currentWeather : (List<Weather_current_entity>)(List<?>) currentWeatherDataList){
            currentweather_templist.add(new Mav_XYPlotData(currentWeather.getRequest_timestamp(), currentWeather.getTemp()));
        }
        mav.addObject("currentweather_templist", currentweather_templist);
    }
}
