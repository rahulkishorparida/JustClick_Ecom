package com.jc.demo.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jc.demo.model.WeatherResponse;
import com.jc.demo.service.WeatherService;
@Service
public class WeatherServiceImpl implements WeatherService{
	
    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
  //  RestTemplate is a Spring utility for making HTTP calls.
   // It's used here to call the external OpenWeatherMap API.

	@Override
	public WeatherResponse getWeatherByCity(String city) {
	    String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&appid=" + apiKey + "&units=metric";
	    
        return restTemplate.getForObject(url, WeatherResponse.class);
	}    
}

//api.openweathermap.org/data/2.5/weather?q=Bhubaneswar&appid=7665b1e19ddc3e4892b91af4690ed2eb&units=metric
