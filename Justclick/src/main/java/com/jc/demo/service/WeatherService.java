package com.jc.demo.service;

import org.springframework.stereotype.Service;

import com.jc.demo.model.WeatherResponse;


public interface WeatherService {
	
	 WeatherResponse getWeatherByCity(String city);
}
//codinate 