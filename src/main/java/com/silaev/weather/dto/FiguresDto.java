package com.silaev.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Dto for working with openweathermap downstream service
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FiguresDto {
    @JsonProperty(value = "temp")
    private BigDecimal temperature;

    @JsonProperty(value = "pressure")
    private BigDecimal pressure;
}
