package com.djw.weatherApp.client.impl;

import com.djw.weatherApp.client.WeatherDataClient;
import com.djw.weatherApp.domain.dto.WeatherAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OpenWeatherMapDataClientImpl implements WeatherDataClient {

    @Value("${openWeather.api.key}")
    private String API_KEY;

    private final WebClient webClient;

    @Override
    public Mono<WeatherAPIResponse> fetchWeatherDataFromOpenWeatherMap(String city){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("appid", API_KEY)
                        .queryParam("q", city)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherAPIResponse.class);
    }
}
