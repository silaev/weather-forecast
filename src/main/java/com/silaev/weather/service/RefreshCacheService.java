package com.silaev.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshCacheService {
    private final WeatherService weatherService;

    /**
     * Refreshes a cities cache at intervals of a refresh-delay-cities.
     * Demonstrates a workaround to caffeine's spec refreshAfterWrite.
     */
    @Scheduled(fixedDelayString = "${config.refresh-delay-cities}")
    private void refreshCacheCities() {
        log.debug("refreshCacheCities");

        weatherService.getCities();
    }
}
