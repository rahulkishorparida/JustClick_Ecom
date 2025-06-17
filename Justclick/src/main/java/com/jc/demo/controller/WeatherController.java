package com.jc.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jc.demo.model.WeatherResponse;
import com.jc.demo.service.WeatherService;



@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/view")
    public String showWeatherByQuery(@RequestParam("city") String city, Model model) {
        try {
            WeatherResponse weather = weatherService.getWeatherByCity(city);

            if (weather == null || weather.getMain() == null || weather.getName() == null || weather.getName().isEmpty()) {
                model.addAttribute("error", "Invalid city name or area not found. Please try again.");
                model.addAttribute("weather", new WeatherController()); 
                return "weather";
            }

            model.addAttribute("weather", weather);
            return "weather";

        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong while fetching weather.");
            model.addAttribute("weather", new WeatherResponse()); 
            return "weather";
        } 
    }


}


//http:localhost:8080/weather/view?city=Mumbai

