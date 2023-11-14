package com.example.routeplanner;

import com.example.routeplanner.model.Airport;
import com.example.routeplanner.service.AirportService;
import com.example.routeplanner.service.RoutePlanner;
import com.example.routeplanner.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteServiceTest {

    private RouteService routeService;

    @BeforeEach
    void setUp() {
        AirportService airportService = new AirportService();
        RoutePlanner routePlanner = new RoutePlanner();
        routeService = new RouteService(airportService, routePlanner);

        airportService.initAirports();
        routeService.initRoutes();
    }

    @Test
    void testFindShortestPathValidAirports() {
        String startCode = "TLL";
        String endCode = "IST";
        List<Airport> result = routeService.findShortestPath(startCode, endCode);

        assertTrue(result.stream().anyMatch(airport -> airport.getIATA().equals(startCode)));
        assertTrue(result.stream().anyMatch(airport -> airport.getIATA().equals(endCode)));
    }

    @Test
    void testFindShortestPathInvalidStartAirport() {
        String invalidCode = "XYZ";
        String endCode = "TLL";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> routeService.findShortestPath(invalidCode, endCode));

        assertEquals("Invalid start airport code: " + invalidCode, exception.getMessage());
    }

    @Test
    void testFindShortestPathInvalidEndAirport() {
        String startCode = "TLL";
        String invalidCode = "INVALID";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> routeService.findShortestPath(startCode, invalidCode));

        assertEquals("Invalid end airport code: " + invalidCode, exception.getMessage());
    }
}