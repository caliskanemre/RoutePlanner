package com.example.routeplanner.service;

import com.example.routeplanner.exception.AirportNotFoundException;
import com.example.routeplanner.exception.NoRouteFoundException;
import com.example.routeplanner.model.Airport;
import com.example.routeplanner.model.Route;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Service
public class RoutePlanner {

    private final Map<Airport, List<Route>> adjList = new HashMap<>();
    private final Map<Airport, Double> distances = new HashMap<>();
    public void initializeDistances(Collection<Airport> airports) {
        for (Airport airport : airports) {
            distances.put(airport, Double.MAX_VALUE);
        }
    }
    public void addRoute(Route route, Map<String, Airport> airports) {

        String sourceCode = route.getSource();
        String destinationCode = route.getDestination();

        Airport source = airports.get(sourceCode);
        Airport destination = airports.get(destinationCode);

        if(destination != null && source != null){
            if (!distances.containsKey(source) || !distances.containsKey(destination)) {
                throw new IllegalStateException("One of the airports in the route is not loaded: " + source.getIATA() + ", " + destination.getIATA());
            }
            else{
                adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(route);
            }
        }
    }

    private void addGroundRoutes(Map<String, Airport> airports) {
        final double MAX_GROUND_DISTANCE = 100.0; // Max distance for ground transport in km

        for (Airport a1 : airports.values()) {
            for (Airport a2 : airports.values()) {
                if (!a1.equals(a2) && calculateDistance(a1, a2) <= MAX_GROUND_DISTANCE) {
                    // Add ground route between a1 and a2
                    Route groundRoute = new Route(a1, a2, true); // true indicates ground transport
                    adjList.computeIfAbsent(a1, k -> new ArrayList<>()).add(groundRoute);
                }
            }
        }
    }
    //Dijkstra's Algorithm
    public List<Airport> findShortestPath(String startCode, String endCode, Map<String, Airport> airports) {
        Airport start = airports.get(startCode);
        Airport end = airports.get(endCode);

        if (start == null) {
            throw new AirportNotFoundException(startCode);
        }
        if(end == null)
            throw new AirportNotFoundException(endCode);

        initializeDistances(airports.values()); // Initialize distances for all airports
        addGroundRoutes(airports); // Add ground routes

        Map<Airport, Airport> previous = new HashMap<>();
        PriorityQueue<Airport> nodes = new PriorityQueue<>(Comparator.comparing(distances::get));

        distances.put(start, 0.0); // Start airport has a distance of 0
        nodes.add(start);

        while (!nodes.isEmpty()) {
            Airport closest = nodes.poll();

            if (closest.equals(end)) {
                List<Airport> path = buildPath(previous, end);
                if (path.isEmpty()) {
                    throw new NoRouteFoundException(startCode, endCode);
                }else
                    return path;
            }

            for (Route route : adjList.getOrDefault(closest, Collections.emptyList())) {
                Airport neighbor = airports.get(route.getDestination());

                if (neighbor == null) continue;

                double alt = distances.get(closest) + calculateDistance(closest, neighbor);
                if (alt < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distances.put(neighbor, alt);
                    previous.put(neighbor, closest);
                    nodes.remove(neighbor);
                    nodes.add(neighbor);
                }
            }
        }

        return new ArrayList<>(); // No path found
    }


    public List<Airport> buildPath(Map<Airport, Airport> previous, Airport end) {
        List<Airport> path = new ArrayList<>();
        for (Airport at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // Haversine algo
    public double calculateDistance(Airport airport1, Airport airport2) {
        double lat1 = airport1.getLatitude();
        double lon1 = airport1.getLongitude();
        double lat2 = airport2.getLatitude();
        double lon2 = airport2.getLongitude();

        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

}
