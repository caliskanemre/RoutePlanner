package com.example.routeplanner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<String> handleAirportNotFoundException(AirportNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidRouteException.class)
    public ResponseEntity<String> handleInvalidRouteException(InvalidRouteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NoRouteFoundException.class)
    public ResponseEntity<String> handleNoRouteFoundException(NoRouteFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<String> handleExternalServiceException(ExternalServiceException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

    @ExceptionHandler(DataLoadingException.class)
    public ResponseEntity<String> handleDataLoadingException(DataLoadingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error initializing application data.");
    }
}
