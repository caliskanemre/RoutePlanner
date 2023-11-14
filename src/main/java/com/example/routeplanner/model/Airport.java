package com.example.routeplanner.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class Airport {
    String name;
    String city;
    String country;
    String IATA;
    String ICAO;
    double latitude;
    double longitude;
    int altitude;
    float timezone;
}