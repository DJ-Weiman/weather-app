package com.djw.weatherApp.controller;

import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import com.djw.weatherApp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/api/v1/")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public CompletableFuture<ResponseEntity<WeatherSummaryDTO>> getWeatherSummaryForCity(@RequestParam("city") String city){
        return weatherService.getWeatherSummaryForCity(city)
                .thenApply(ResponseEntity::ok);
    }
}
