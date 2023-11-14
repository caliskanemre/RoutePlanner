package com.example.routeplanner.exception;

public class NoRouteFoundException extends RuntimeException {
    public NoRouteFoundException(String startCode, String endCode) {
        super("No route could be found between " + startCode + " and " + endCode + ".");
    }
}
