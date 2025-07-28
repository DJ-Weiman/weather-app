package com.djw.weatherApp.utils;

import com.djw.weatherApp.domain.dto.WeatherAPIResponse;
import com.djw.weatherApp.domain.dto.WeatherSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeatherSummaryCalculator {

    private final DateFormatterUtil dateFormatterUtil;

    public WeatherSummaryDTO processData(List<WeatherAPIResponse.DailyForecast> dailyForecasts){
        if(dailyForecasts.isEmpty())
            throw new RuntimeException("No Daily forecasts available");

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

}
