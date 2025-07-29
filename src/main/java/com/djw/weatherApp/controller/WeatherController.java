package com.djw.weatherApp.controller;

import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import com.djw.weatherApp.service.WeatherService;
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

    @GetMapping("/weather")
    public CompletableFuture<ResponseEntity<WeatherSummaryDTO>> getWeatherSummaryForCity(
            @RequestParam
            @NotBlank(message = "City name cannot be null or empty.")
            @Pattern(regexp = "^[a-zA-Z\\s-']+$",
                    message = "City name contains invalid characters. Only letters, spaces, hyphens, and apostrophes are allowed.")
            String city
    ){
        return weatherService.getWeatherSummaryForCity(city)
                .thenApply(ResponseEntity::ok);
    }
}
