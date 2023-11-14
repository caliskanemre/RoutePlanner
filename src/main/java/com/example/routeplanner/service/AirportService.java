package com.example.routeplanner.service;

import com.example.routeplanner.exception.AirportNotFoundException;
import com.example.routeplanner.exception.DataLoadingException;
import com.example.routeplanner.model.Airport;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

@Getter
@Service
public class AirportService {
    private final Map<String, Airport> airports = new HashMap<>();

    @PostConstruct
    public void initAirports() {
        try {
            ClassPathResource resource = new ClassPathResource("data/airports.dat");
            String data = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            String[] lines = data.split("\\r?\\n");
            for (String line : lines) {
                String[] columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < columns.length; i++) {
                    columns[i] = columns[i].replace("\"", "").trim();
                }
                String iataCode = "\\N".equals(columns[4]) ? null : columns[4]; // Handle \N for IATA code
                float timezone = "\\N".equals(columns[9]) ? 0.0f : Float.parseFloat(columns[9]); // Handle \N for timezone
                Airport airport = new Airport(
                        columns[1], // name
                        columns[2], // city
                        columns[3], // country
                        iataCode,
                        columns[5], // ICAO
                        Double.parseDouble(columns[6]), // latitude
                        Double.parseDouble(columns[7]), // longitude
                        Integer.parseInt(columns[8]), // altitude
                        timezone
                );
                airports.put(airport.getIATA(), airport);
            }
        }  catch (IOException e) {
            throw new DataLoadingException("Failed to load airports from airports.dat", e);
        }
    }

    public Airport getAirportByCode(String code) {
        Airport airport = airports.get(code);
        if (airport == null) {
            throw new AirportNotFoundException(code);
        }
        return airport;
    }
}

