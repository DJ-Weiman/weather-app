package com.djw.weatherApp.service.impl;

import com.djw.weatherApp.client.WeatherDataClient;
import com.djw.weatherApp.domain.dto.WeatherAPIResponse;
import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import com.djw.weatherApp.service.WeatherService;
import com.djw.weatherApp.utils.DateFormatterUtil;
import com.djw.weatherApp.utils.WeatherSummaryCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherDataClient weatherDataClient;
    private final WeatherSummaryCalculator weatherSummaryCalculator;

    @Override
    @Async
    @Cacheable("weatherSummary")
    public CompletableFuture<WeatherSummaryDTO> getWeatherSummaryForCity(String city){
        return weatherDataClient.fetchWeatherDataFromOpenWeatherMap(city).map(weatherAPIResponse -> {
            if (weatherAPIResponse == null || weatherAPIResponse.getDaily() == null || weatherAPIResponse.getDaily().isEmpty()) {
                throw new RuntimeException("Could not retrieve weather data for " + city);
            }

            List<WeatherAPIResponse.DailyForecast> forecastList = weatherAPIResponse.getDaily().stream().collect(Collectors.toList());

            WeatherSummaryDTO weatherSummaryDTO = weatherSummaryCalculator.processData(forecastList);
            weatherSummaryDTO.setCity(city);
            return weatherSummaryDTO;
        }).toFuture();
    }
}
