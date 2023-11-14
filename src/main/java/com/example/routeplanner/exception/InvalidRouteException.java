package com.example.routeplanner.exception;

public class InvalidRouteException extends RuntimeException {
    public InvalidRouteException(String IATA1, String IATA2) {
        super("One of the airports in the route is not loaded: "+IATA1+ " and " + IATA2);
    }
}
