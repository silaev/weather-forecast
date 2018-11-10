package com.silaev.weather.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silaev.weather.exception.BusinessException;
import com.silaev.weather.model.DayPart;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;

public class WeatherUtil {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    static final BigDecimal BIG_DECIMAL_ZERO = setScaleAndRound(BigDecimal.ZERO);

    private WeatherUtil() {
    }

    public static boolean isOffsetBeforeOrEqual(Temporal temporal1Inclusive, Temporal temporal2Exclusive, int lastDays) {
        long hours = Duration.between(temporal1Inclusive, temporal2Exclusive).toHours();
        return hours >=0 && hours <= Duration.ofDays(lastDays).toHours();
    }

    static DayPart getDayPart(Instant temporal) {
        int hour = LocalDateTime.ofInstant(temporal, ZoneOffset.UTC).getHour();

        if ((hour>=6)&&(hour<18)) {
            return DayPart.DAILY;
        }

        return DayPart.NIGHTLY;
    }

    static BigDecimal setScaleAndRound(BigDecimal value) {
        return value.setScale(SCALE, ROUNDING_MODE);
    }

    public static HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new HttpEntity<>(headers);
    }

    public static <T> T loadObjectFromJsonFile(String path, ObjectMapper mapper, JavaType type) {
        mapper.findAndRegisterModules();
        Resource resource = new ClassPathResource(path);
        T obj;
        try {
            obj = mapper.readValue(resource.getInputStream(), type);
        } catch (IOException e) {
            throw new BusinessException(String.format("Cannot read resource: %s", path), e);
        }
        return obj;
    }
}
