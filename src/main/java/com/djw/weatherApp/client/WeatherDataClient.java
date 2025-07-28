package com.djw.weatherApp.client;

import com.djw.weatherApp.domain.dto.WeatherAPIResponse;
import reactor.core.publisher.Mono;

public interface WeatherDataClient {
    Mono<WeatherAPIResponse> fetchWeatherDataFromOpenWeatherMap(String city);
}
