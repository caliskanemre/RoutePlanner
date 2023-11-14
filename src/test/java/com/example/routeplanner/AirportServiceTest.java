package com.example.routeplanner;

import static org.junit.jupiter.api.Assertions.*;

import com.example.routeplanner.model.Airport;
import com.example.routeplanner.service.AirportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

class AirportServiceTest {

    private AirportService airportService;

    @BeforeEach
    void setUp() {
        airportService = new AirportService();
        airportService.initAirports();
    }

    @Test
    void testAirportInitialization() {
        Map<String, Airport> airports = airportService.getAirports();
        assertNotNull(airports);
        assertFalse(airports.isEmpty());
    }

    @Test
    void testGetAirportByCode() {
        String testCode = "TLL";
        Airport airport = airportService.getAirportByCode(testCode);
        assertNotNull(airport);
        assertEquals(testCode, airport.getIATA());
    }
}
