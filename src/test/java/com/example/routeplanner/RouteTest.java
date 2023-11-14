package com.example.routeplanner;

import com.example.routeplanner.model.Airport;
import com.example.routeplanner.model.Route;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void testCreateRouteWithAirports() {
        Airport sourceAirport = new Airport("Sivas Airport", "Sivas", "Turkey", "VAS", "ICAO1", 0.0, 0.0, 0, 0.0f);
        Airport destinationAirport = new Airport("Istanbul Airport", "istanbul", "Turkey", "IST", "ICAO2", 0.0, 0.0, 0, 0.0f);
        Route route = new Route(sourceAirport, destinationAirport, false);

        assertEquals("VAS", route.getSource());
        assertEquals("IST", route.getDestination());
        assertFalse(route.isGroundTransport());
    }

    @Test
    void testCreateRouteWithDetails() {
        String airline = "Baltic";
        String airlineID = "123";
        String source = "VAS";
        String sourceID = "45622";
        String destination = "IST";
        String destinationID = "712389";
        boolean codeshare = true;
        int stops = 0;
        boolean isGroundTransport = false;

        Route route = new Route(airline, airlineID, source, sourceID, destination, destinationID, codeshare, stops, isGroundTransport);

        assertEquals(airline, route.getAirline());
        assertEquals(airlineID, route.getAirlineID());
        assertEquals(source, route.getSource());
        assertEquals(sourceID, route.getSourceID());
        assertEquals(destination, route.getDestination());
        assertEquals(destinationID, route.getDestinationID());
        assertTrue(route.isCodeshare());
        assertEquals(stops, route.getStops());
        assertFalse(route.isGroundTransport());
    }

    @Test
    void testRouteEquality() {
        Route route1 = new Route("Baltic", "123", "TLL", "456", "HLL", "789", true, 0, false);
        Route route2 = new Route("Baltic", "123", "TLL", "456", "HLL", "789", true, 0, false);

        assertEquals(route1, route2);
    }
}
