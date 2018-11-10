package com.silaev.weather.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Add config.last-days to current Instant.now() by
 * truncating it to hours to correspond with openweathermap service
 * (keeping dates and times with precision of hour)
 */
@Component
public class DateTimeConverter {
    private static int LAST_DAYS;

    @Value("${config.last-days}")
    public void setLastDays(int lastDays) {
        LAST_DAYS = lastDays;
    }

    public Instant getInstantThreshold() {
        return Instant.now()
                .truncatedTo(ChronoUnit.HOURS)
                .plus(Duration.ofDays(LAST_DAYS));
    }
}
