package com.djw.weatherApp.controller;

import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import com.djw.weatherApp.service.WeatherService;
import com.djw.weatherApp.utils.CityNameValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/api/v1/")
@Validated
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final CityNameValidator cityNameValidator;

    @GetMapping("/weather")
    public CompletableFuture<ResponseEntity<WeatherSummaryDTO>> getWeatherSummaryForCity(@RequestParam String city){
        cityNameValidator.validateCityName(city);
        return weatherService.getWeatherSummaryForCity(city)
                .thenApply(ResponseEntity::ok);
    }
}
