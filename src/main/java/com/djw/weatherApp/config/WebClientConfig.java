package com.djw.weatherApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;


@Configuration
public class WebClientConfig {

    @Value("${openWeather.api.baseUrl}")
    private String BASE_URL;

    @Bean
    public WebClient webClient(){
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(15));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(BASE_URL)
                .build();
    }
}
