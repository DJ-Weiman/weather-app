package com.djw.weatherApp.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAPIResponse {
    // This assumes you're using the One Call API or a similar endpoint
    // that provides a 'daily' array of forecasts.
    @JsonProperty("list")
    private List<DailyForecast> daily;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyForecast {
        @JsonProperty("dt")
        private Long dt;

        @JsonProperty("main")
        private Main main;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Main {

        @JsonProperty("temp")
        private Double temp;    // Day temperature

    }
}



