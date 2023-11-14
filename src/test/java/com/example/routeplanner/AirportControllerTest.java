package com.example.routeplanner;
import com.example.routeplanner.controller.AirportController;
import com.example.routeplanner.service.AirportService;
import com.example.routeplanner.service.RouteService;
import com.example.routeplanner.model.Airport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AirportControllerTest {

    @Mock
    private AirportService airportService;

    @Mock
    private RouteService routeService;

    @InjectMocks
    private AirportController airportController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(airportController).build();
    }

    @Test
    void whenValidAirports_thenReturnRoute() throws Exception {
        // Arrange
        Airport startAirport = new Airport("Lennart Meri Tallinn Airport","Tallinn-ulemiste International","Estonia","TLL","EETN",59.41329956049999,24.832799911499997,131,2);
        Airport endAirport = new Airport("Istanbul Airport","Istanbul","Turkey","IST","LTFM",41.275278,28.751944,325,3);
        List<Airport> expectedPath = Arrays.asList(startAirport, endAirport);

        when(airportService.getAirportByCode("TLL")).thenReturn(startAirport);
        when(airportService.getAirportByCode("IST")).thenReturn(endAirport);
        when(routeService.findShortestPath("TLL", "IST")).thenReturn(expectedPath);

        // Act & Assert
        mockMvc.perform(get("/route?start=TLL&end=IST"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].iata").value("TLL"))
                .andExpect(jsonPath("$[1].iata").value("IST"));
    }

    @Test
    void whenInvalidStartAirport_thenReturnBadRequest() throws Exception {
        // Arrange
        when(airportService.getAirportByCode("INVALID")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/route?start=INVALID&end=IST"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid IATA code provided."));
    }

    @Test
    void whenNoRouteFound_thenReturnNotFound() throws Exception {
        // Arrange
        when(airportService.getAirportByCode("TLL")).thenReturn(new Airport("Lennart Meri Tallinn Airport","Tallinn-ulemiste International","Estonia","TLL","EETN",59.41329956049999,24.832799911499997,131,2));
        when(airportService.getAirportByCode("IST")).thenReturn(new Airport("Istanbul Airport","Istanbul","Turkey","IST","LTFM",41.275278,28.751944,325,3));
        when(routeService.findShortestPath("TLL", "IST")).thenReturn(emptyList());

        // Act & Assert
        mockMvc.perform(get("/route?start=TLL&end=IST"))
                .andExpect(status().isNotFound());
    }

}
