package com.djw.weatherApp.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatterUtil {

    public String convertToLocalDateTime(Long epochTime){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return epochTime != null ?
                LocalDate.ofInstant(Instant.ofEpochSecond(epochTime), ZoneOffset.UTC).format(formatter) : null;
    }
}
