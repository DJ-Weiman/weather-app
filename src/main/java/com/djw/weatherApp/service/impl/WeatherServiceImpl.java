package com.djw.weatherApp.service.impl;

import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import com.djw.weatherApp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Override
    public WeatherSummaryDTO getWeatherSummaryForCity(String city) {
        return null;
    }
}
