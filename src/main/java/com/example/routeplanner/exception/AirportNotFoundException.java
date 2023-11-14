package com.example.routeplanner.exception;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String code) {
        super("Airport with code " + code+ " not found.");
    }
}