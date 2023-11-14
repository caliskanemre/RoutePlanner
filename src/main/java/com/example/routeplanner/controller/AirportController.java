package com.example.routeplanner.controller;

import com.example.routeplanner.model.Airport;
import com.example.routeplanner.service.AirportService;
import com.example.routeplanner.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AirportController {

    private final AirportService airportService;
    private final RouteService routeService;

    public AirportController(AirportService airportService, RouteService routeService) {
        this.airportService = airportService;
        this.routeService = routeService;
    }

    @GetMapping("/route")
    public ResponseEntity<?> getRoute(@RequestParam String start, @RequestParam String end) {
        Airport startAirport = airportService.getAirportByCode(start.toUpperCase());
        Airport endAirport = airportService.getAirportByCode(end.toUpperCase());

        if (startAirport == null || endAirport == null) {
            return ResponseEntity.badRequest().body("Invalid IATA code provided.");
        }

        List<Airport> shortestPathAirports = routeService.findShortestPath(startAirport.getIATA(), endAirport.getIATA());

        if (shortestPathAirports.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(shortestPathAirports);
    }
}
