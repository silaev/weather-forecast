package com.silaev.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Keeps average figures to represent to a client
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AverageDto {
    private BigDecimal dailyAverage;
    private BigDecimal nightlyAverage;
    private BigDecimal pressureAverage;
}
