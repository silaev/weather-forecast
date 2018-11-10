package com.silaev.weather.exception;

public class DownStreamServiceException extends RuntimeException{
    public DownStreamServiceException(String message) {
        super(message);
    }

    public DownStreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
