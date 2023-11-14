package com.example.routeplanner.service;

import com.example.routeplanner.exception.DataLoadingException;
import com.example.routeplanner.exception.NoRouteFoundException;
import com.example.routeplanner.model.Airport;
import com.example.routeplanner.model.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class RouteService {
    private final RoutePlanner routePlanner;
    private final Map<String, Airport> airports;

    public RouteService(AirportService airportService, RoutePlanner routePlanner) {
        this.airports = airportService.getAirports();
        this.routePlanner = routePlanner;
    }

    @PostConstruct
    public void initRoutes() {
        try {
            ClassPathResource resource = new ClassPathResource("data/routes.dat");
            String data = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            String[] lines = data.split("\\r?\\n");
            routePlanner.initializeDistances(airports.values());
            for (String line : lines) {
                String[] routeData = line.split(",", -1); // Correctly splits even if some fields are empty

                Route route = fromDataLine(routeData);
                routePlanner.addRoute(route, airports);
            }
        } catch (IOException e) {
            throw new DataLoadingException("Failed to load routes from routes.dat", e);
        }
    }

    public static Route fromDataLine(String[] data) {
        String airline = data[0];
        String airlineID = data[1];
        String source = data[2];
        String sourceID = data[3];
        String destination = data[4];
        String destinationID = data[5];
        boolean codeshare = "Y".equals(data[6]);
        int stops = data[7].isEmpty() ? 0 : Integer.parseInt(data[7]);
        return new Route(airline, airlineID, source, sourceID, destination, destinationID, codeshare, stops,false);
    }

    public List<Airport> findShortestPath(String startCode, String endCode) {
        Airport start = airports.get(startCode);
        Airport end = airports.get(endCode);

        if (start == null) {
            throw new IllegalArgumentException("Invalid start airport code: " + startCode);
        }
        if (end == null) {
            throw new IllegalArgumentException("Invalid end airport code: " + endCode);
        }

        List<Airport> shortestPath = routePlanner.findShortestPath(startCode, endCode, airports);
        if (shortestPath.isEmpty()) {
            throw new NoRouteFoundException(startCode, endCode);
        }

        return shortestPath;
    }
}
