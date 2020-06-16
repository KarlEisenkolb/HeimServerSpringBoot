package com.pi.server.GuiController_out;

import com.pi.server.GuiServices_out.MainService;
import com.pi.server.GuiServices_out.ServerDataStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Random;

@RestController
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private ServerDataStatusService serverDataStatusService;

    private final int anzahl_Tageskacheln = 7; // anzahl an weiteren tagen nach Heute
    private final Logger log = LoggerFactory.getLogger(MainController.class);
    private final String TEMPLATE_MAIN = "main";

    @RequestMapping("/")
    public ModelAndView mainTemplate(){
        ModelAndView mav = new ModelAndView(TEMPLATE_MAIN);

        mav.addObject("time_and_date_string_long", mainService.getTimeAndDateString(mainService.DATE_LONG));
        mav.addObject("time_and_date_string_short", mainService.getTimeAndDateString(mainService.DATE_SHORT));

        weatherData(mav);
        organisationsappData(mav);
        return mav;
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
