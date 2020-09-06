package com.pi.server.GuiController_out;

import com.pi.server.GuiServices_out.MainService;
import com.pi.server.GuiServices_out.ServerDataStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MainService mainService;

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
}
