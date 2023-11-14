# Route Planner Service

## Overview
This project is a route planning service designed to find the shortest path between airports. It supports finding routes with a maximum of 4 legs (3 stops/layovers) and also allows changing airports during stops if they are within 100 km of each other.

## Features
- Finds routes between two given airports (IATA/ICAO codes).
- Supports routes with up to 3 stops/layovers.
- Includes ground transportation options for airports within 100 km.
- Utilizes geographical distance calculations for route optimization.

## Technologies
- Java
- Spring Boot
- JUnit and Mockito for unit testing

## Setup and Installation
To set up the project on your local machine, follow these steps:

1. Clone the repository:
   git clone

2. gradle build
3. gradle bootRun

## API Endpoints
#### Find Route: GET /route?start={startAirportCode}&end={endAirportCode}

Finds the shortest route between the start and end airports.
Parameters:
##### startAirportCode: IATA/ICAO code of the start airport.
##### endAirportCode: IATA/ICAO code of the end airport.