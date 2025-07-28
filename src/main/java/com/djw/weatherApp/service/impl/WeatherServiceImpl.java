package com.djw.weatherApp.service.impl;

import com.djw.weatherApp.domain.dto.WeatherAPIResponse;
import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import com.djw.weatherApp.service.WeatherService;
import com.djw.weatherApp.utils.DateFormatterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${openWeather.api.key}")
    private String API_KEY;

    private final WebClient webClient;
    private final DateFormatterUtil dateFormatterUtil;

    @Override
    @Async
    public CompletableFuture<WeatherSummaryDTO> getWeatherSummaryForCity(String city){
        return fetchWeatherDataFromOpenWeatherMap(city).map(weatherAPIResponse -> {
            if (weatherAPIResponse == null || weatherAPIResponse.getDaily() == null || weatherAPIResponse.getDaily().isEmpty()) {
                throw new RuntimeException("Could not retrieve weather data for " + city);
            }

            List<WeatherAPIResponse.DailyForecast> forecastList = weatherAPIResponse.getDaily().stream().collect(Collectors.toList());

            WeatherSummaryDTO weatherSummaryDTO = processData(forecastList);
            weatherSummaryDTO.setCity(city);
            return weatherSummaryDTO;
        }).toFuture();
    }

    private WeatherSummaryDTO processData(List<WeatherAPIResponse.DailyForecast> dailyForecasts){
        double totalTemperature = 0.0;
        WeatherAPIResponse.DailyForecast hottestDayForecast = null;
        WeatherAPIResponse.DailyForecast coldestDayForecast = null;

        for (WeatherAPIResponse.DailyForecast forecast : dailyForecasts) {
            double dayTemp = forecast.getMain().getTemp();
            totalTemperature += dayTemp;

            if (hottestDayForecast == null || dayTemp > hottestDayForecast.getMain().getTemp()) {
                hottestDayForecast = forecast;
            }
            if (coldestDayForecast == null || dayTemp < coldestDayForecast.getMain().getTemp()) {
                coldestDayForecast = forecast;
            }
        }

        double averageTemperature = totalTemperature / dailyForecasts.size();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String hottestDay = dateFormatterUtil.convertToLocalDateTime(hottestDayForecast.getDt());
        String coldestDay = dateFormatterUtil.convertToLocalDateTime(coldestDayForecast.getDt());

        return WeatherSummaryDTO.builder()
                .averageTemperature(averageTemperature)
                .coldestDay(coldestDay)
                .hottestDay(hottestDay)
                .build();
    }

    private Mono<WeatherAPIResponse> fetchWeatherDataFromOpenWeatherMap(String city){
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
