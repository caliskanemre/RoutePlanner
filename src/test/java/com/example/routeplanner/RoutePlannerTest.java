package com.example.routeplanner;

import com.example.routeplanner.model.Airport;
import com.example.routeplanner.model.Route;
import com.example.routeplanner.service.AirportService;
import com.example.routeplanner.service.RoutePlanner;
import com.example.routeplanner.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class RoutePlannerTest {

    private RoutePlanner routePlanner;
    private Map<String, Airport> airports;

    @BeforeEach
    void setUp() {
        AirportService airportService = new AirportService();
        routePlanner = new RoutePlanner();
        RouteService routeService = new RouteService(airportService, routePlanner);

        airportService.initAirports();
        routeService.initRoutes();
        airports = airportService.getAirports();
    }

    @Test
    void testInitializeDistances() {
        routePlanner.initializeDistances(airports.values());

        for (Airport airport : airports.values()) {
            assertTrue(routePlanner.getDistances().containsKey(airport),
                    "Distances map should contain the airport: " + airport.getIATA());
            assertEquals(Double.MAX_VALUE, routePlanner.getDistances().get(airport),
                    "Distance for airport " + airport.getIATA() + " should be initialized to Double.MAX_VALUE");
        }
    }

    @Test
    void testAddRoute() {
        Route route = new Route("THY", "SA", "TLL", "123", "IST", "456", false, 0, false);
        routePlanner.addRoute(route, airports);

        assertTrue(routePlanner.getAdjList().containsKey(airports.get("TLL")), "Adjacency list should contain the source airport");
        List<Route> routesFromJFK = routePlanner.getAdjList().get(airports.get("TLL"));
        assertNotNull(routesFromJFK, "There should be routes from TLL");
        assertTrue(routesFromJFK.contains(route), "Routes from TLL should include the added route");
    }

    @Test
    void testFindShortestPath() {
        // Setup - adding routes
        // Assuming direct route for simplicity
        Route route = new Route("DELTA", "SA", "IST", "123", "TLL", "456", false, 0, false);
        routePlanner.addRoute(route, airports);

        List<Airport> result = routePlanner.findShortestPath("IST", "TLL", airports);

        // Assert the result of findShortestPath
        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "The result should not be empty");
        assertEquals("IST", result.get(0).getIATA(), "The path should start at IST");
        assertEquals("TLL", result.get(result.size() - 1).getIATA(), "The path should end at TLL");
    }

    @Test
    void testBuildPath() {
        // Manually creating a path in the 'previous' map
        Map<Airport, Airport> previous = new HashMap<>();
        Airport jfk = airports.get("IST");
        Airport lax = airports.get("TLL");
        previous.put(lax, jfk); // Direct path for simplicity

        List<Airport> path = routePlanner.buildPath(previous, lax);

        // Assert the path is correct
        assertEquals(2, path.size(), "Path should include two airports");
        assertEquals(jfk, path.get(0), "Path should start with IST");
        assertEquals(lax, path.get(1), "Path should end with TLL");
    }
}
