package com.silaev.weather.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;


/**
 * Handles exception withing the whole application.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidFormatException.class, ValidationException.class})
    public ResponseEntity<String> nonProcessableEntityException(RuntimeException rte) {
        log.debug("nonProcessableEntityException");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(rte.getMessage());
    }

    @ExceptionHandler({JsonParseException.class})
    public ResponseEntity<String> invalidJsonEntityException(RuntimeException rte) {
        log.debug("invalidJsonEntityException");
        return ResponseEntity.badRequest().body(rte.getMessage());
    }

    @ExceptionHandler(DownStreamServiceException.class)
    public ResponseEntity<String> downStreamServiceException(RuntimeException rte) {
        log.debug("downStreamServiceException");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(rte.getMessage());
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> businessException(RuntimeException rte) {
        log.debug("businessException");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(rte.getMessage());
    }
}
