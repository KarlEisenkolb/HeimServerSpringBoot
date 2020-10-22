package com.pi.server.GuiController_out;

import com.pi.server.GuiServices_out.MainService;
import com.pi.server.GuiServices_out.SensorService;
import com.pi.server.GuiServices_out.ServerDataStatusService;
import com.pi.server.Models.SensorModels.Mav_XYPlotData;
import com.pi.server.Models.SensorModels.Sensor_BME680_entity;
import com.pi.server.Models.SensorModels.Sensor_Particle_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @EventListener(ApplicationReadyEvent.class)
    public void getServerDataUrlAfterServerStart(){
            WEBSITE_GET_DATA_STATUS_URL = "http://" + serverIp + ":" + serverPort + "/data_status";
            System.out.println(WEBSITE_GET_DATA_STATUS_URL);
    }

    @RequestMapping("/")
    public ModelAndView mainTemplate(){
        ModelAndView mav = new ModelAndView(TEMPLATE_MAIN);

        basicData(mav);
        weatherData(mav);
        organisationsappData(mav);
        sensorData(mav);
        return mav;
    }

    private void basicData(ModelAndView mav) {
        mav.addObject("website_get_data_status_url", WEBSITE_GET_DATA_STATUS_URL);
        mav.addObject("time_and_date_string_long", mainService.getTimeAndDateString(mainService.DATE_LONG));
        mav.addObject("time_and_date_string_short", mainService.getTimeAndDateString(mainService.DATE_SHORT));
    }

    @RequestMapping("/data_status")
    public String checkForNewData(){
        return serverDataStatusService.getServerStatus();
    }

    private void organisationsappData(ModelAndView mav) {
        List dayterminlist = mainService.getTermineForDaysToDisplay(anzahl_Tageskacheln);
        mav.addObject("currentdayterminlist", dayterminlist.get(0));
        dayterminlist.remove(0);
        mav.addObject("dayterminlist", dayterminlist);
    }

    private void weatherData(ModelAndView mav) {

        List weatherList = mainService.getCurrentWeatherContent();
        mav.addObject("latestWeather", weatherList.get(0));
        mav.addObject("currentweatherlist", weatherList);

        List weatherHourlyList = mainService.getWeatherHourlyForecastContent();
        mav.addObject("hourlyweatherlist", weatherHourlyList);

        List weatherDailyList = mainService.getWeatherDailyForecastContent();
        weatherDailyList.remove(0);
        mav.addObject("dailyweatherlist", weatherDailyList);
        mav.addObject("headingsdates", mainService.getDateHeadingStrings(anzahl_Tageskacheln));
    }

    private void sensorData(ModelAndView mav){

        List bme680List = sensorService.getBme680Content();
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


        List particleList = sensorService.getParticleContent();
        List<Mav_XYPlotData> pm25   = new ArrayList<>();
        List<Mav_XYPlotData> pm10   = new ArrayList<>();

        for(Sensor_Particle_entity particle : (List<Sensor_Particle_entity>)(List<?>) particleList){
            pm25.add(new Mav_XYPlotData(particle.getId(), particle.getPm25()));
            pm10.add(new Mav_XYPlotData(particle.getId(), particle.getPm10()));
        }
        mav.addObject("pm25", pm25);
        mav.addObject("pm10", pm10);
    }
}
