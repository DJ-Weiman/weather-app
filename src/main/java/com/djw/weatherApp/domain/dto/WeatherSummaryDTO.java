package com.djw.weatherApp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherSummaryDTO {
    private String city;
    private Double averageTemperature;
    private String hottestDay;
    private String coldestDay;
}
