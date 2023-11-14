package com.example.routeplanner.model;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Route {
    private String airline;
    private String airlineID;
    private String source;
    private String sourceID;
    private String destination;
    private String destinationID;
    private boolean codeshare; // This might be a boolean (true/false) or String, depending on your data
    private int stops;
    private boolean isGroundTransport;

    public Route(Airport source, Airport destination, boolean isGroundTransport) {
        this.source = source.IATA;
        this.destination = destination.IATA;
        this.isGroundTransport = isGroundTransport;
    }
}