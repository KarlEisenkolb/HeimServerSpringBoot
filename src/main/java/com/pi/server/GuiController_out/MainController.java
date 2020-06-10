package com.pi.server.GuiController_out;

import com.pi.server.GuiServices_out.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MainService mainService;

    private final String TEMPLATE_MAIN = "main";

    @RequestMapping("/")
    public ModelAndView mainTemplate(){

        ModelAndView mav = new ModelAndView(TEMPLATE_MAIN);
        mav.addObject("time_and_date_string", mainService.getTimeAndDateString());

        List weatherList = mainService.getCurrentWeatherContent();
        mav.addObject("latestWeather", weatherList.get(0));
        mav.addObject("currentweatherlist", weatherList);

        List weatherHourlyList = mainService.getWeatherHourlyForecastContent();
        mav.addObject("hourlyweatherlist", weatherHourlyList);

        List weatherDailyList = mainService.getWeatherDailyForecastContent();
        weatherDailyList.remove(0);
        mav.addObject("dailyweatherlist", weatherDailyList);
        mav.addObject("headingsdates", mainService.getDateHeadingStrings(weatherDailyList));

        return mav;
    }
}
