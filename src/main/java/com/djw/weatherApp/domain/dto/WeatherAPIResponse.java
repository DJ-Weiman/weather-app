package com.djw.weatherApp.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WeatherAPIResponse {
    @JsonProperty("main")
    private Main main;

    @Data
    public static class Main {
        @JsonProperty("temp")
        private Double temp;
    }
}