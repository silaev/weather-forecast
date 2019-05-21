package com.silaev.weather.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silaev.weather.converter.CityConverter;
import com.silaev.weather.converter.WeatherSummaryStatisticsConverter;
import com.silaev.weather.dto.AverageDto;
import com.silaev.weather.dto.CityDto;
import com.silaev.weather.dto.ForecastResponseDto;
import com.silaev.weather.exception.DownStreamServiceException;
import com.silaev.weather.model.City;
import com.silaev.weather.util.WeatherSummaryStatistics;
import com.silaev.weather.util.WeatherUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Serves all REST operation as per WeatherController
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private static int LAST_DAYS;
    private static String URL;
    private static String APP_ID;
    private final RestTemplate restTemplate;
    private final WeatherSummaryStatisticsConverter statisticsConverter;
    private final CityConverter cityConverter;

    @Value("${config.last-days}")
    public void setLastDays(int lastDays) {
        LAST_DAYS = lastDays;
    }

    @Value("${config.url}")
    public void setURL(String url) {
        URL = url;
    }

    @Value("${config.appId}")
    public void setAppId(String appId) {
        APP_ID = appId;
    }

    @Cacheable(value = "average", key = "{#instantThreshold, #cityCountryName}")
    public AverageDto getAverageTemperaturesAndPressure(String cityCountryName, Instant instantThreshold) {
        log.debug("getAverageTemperaturesAndPressure");

        ForecastResponseDto forecastResponseDto = getForecastResponseDto(cityCountryName);
        return computeAverageFigures(forecastResponseDto, instantThreshold);
    }

    private AverageDto computeAverageFigures(ForecastResponseDto forecastResponseDto, Instant instantThreshold) {
        log.debug("computeAverageFigures");

        WeatherSummaryStatistics summaryStatistics = forecastResponseDto.getForecastDtos().stream()
                .parallel()
                .filter(f -> WeatherUtil.isOffsetBeforeOrEqual(f.getTimestamp(), instantThreshold, LAST_DAYS))
                //.peek(x->log.debug(x.toString()))
                .collect(WeatherSummaryStatistics.statistics());

        return statisticsConverter.convert(summaryStatistics);
    }

    private ForecastResponseDto getForecastResponseDto(String cityCountryName) {
        log.debug("getForecastResponseDto");

        HttpEntity<?> entity = WeatherUtil.getHttpEntity();

        //query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("q", cityCountryName)
                .queryParam("units", "metric")
                .queryParam("appid", APP_ID);


        HttpEntity<ForecastResponseDto> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                ForecastResponseDto.class);


        ForecastResponseDto body = response.getBody();
        int responseCode = Objects.requireNonNull(body).getResponseCode();
        if (responseCode != HttpStatus.OK.value()) {
            throw new DownStreamServiceException(
                    String.format("Got a response code: %d from %s", responseCode, URL));
        }
        return body;
    }

    @Cacheable("cities")
    public Set<CityDto> getCities() {
        log.debug("a new call to getCities");

        String path = "city.list.json";
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().
                constructCollectionType(Set.class, City.class);
        Set<City> cities = WeatherUtil.loadObjectFromJsonFile(path, mapper, type);
        return cities.stream().map(cityConverter::convert).collect(Collectors.toSet());
    }
}
