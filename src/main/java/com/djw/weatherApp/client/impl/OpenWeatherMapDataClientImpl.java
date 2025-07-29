package com.djw.weatherApp.client.impl;

import com.djw.weatherApp.client.WeatherDataClient;
import com.djw.weatherApp.domain.dto.WeatherAPIResponse;
import com.djw.weatherApp.exception.OpenWeatherMapApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
                .bodyToMono(WeatherAPIResponse.class)
                .onErrorMap(WebClientResponseException.class, ex -> {
                    System.err.println("WebClient error calling OpenWeatherMap API: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
                    return new OpenWeatherMapApiException("Failed to fetch data from OpenWeatherMap API. Status: " + ex.getStatusCode());
                })
                .doOnError(throwable -> {
                    if (!(throwable instanceof OpenWeatherMapApiException)) {
                        System.err.println("Generic error calling OpenWeatherMap API: " + throwable.getMessage());
                        throw new OpenWeatherMapApiException("An unexpected error occurred while calling OpenWeatherMap API.");
                    }
                });
    }
}
