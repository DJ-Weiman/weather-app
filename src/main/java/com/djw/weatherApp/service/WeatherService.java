package com.djw.weatherApp.service;

import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;

public interface WeatherService {
    WeatherSummaryDTO getWeatherSummaryForCity(String city);
}
