package com.silaev.weather.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silaev.weather.dto.AverageDto;
import com.silaev.weather.dto.ForecastResponseDto;
import com.silaev.weather.model.City;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

public class TestUtil {
    private TestUtil() {
    }

    public static AverageDto mockAverageDto(BigDecimal dailyAverage,
                                            BigDecimal nightlyAverage,
                                            BigDecimal pressureAverage) {
        return AverageDto.builder()
                .dailyAverage(dailyAverage)
                .nightlyAverage(nightlyAverage)
                .pressureAverage(pressureAverage)
                .build();
    }

    /**
     * Fr integration tests
     * @param param
     * @return
     */
    public static String encodeQueryParam(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("Cannot encodeQueryParam %s", param));
    }

    public static ForecastResponseDto mockForecastResponseDto() {
        String path = "forecast.json";
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().constructType(ForecastResponseDto.class);
        return WeatherUtil.loadObjectFromJsonFile(path, mapper, type);
    }

}
