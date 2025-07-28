package com.djw.weatherApp.service;

import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;

import java.util.concurrent.CompletableFuture;

public interface WeatherService {
    CompletableFuture<WeatherSummaryDTO> getWeatherSummaryForCity(String city);
}
