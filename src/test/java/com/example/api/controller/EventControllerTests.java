package com.example.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCreateEvent() throws Exception {
        String locationJson = "{ \"slug\": \"new-location\", \"name\": \"New Location\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isCreated());

        String eventJson = "{ \"title\": \"New Event\", \"price\": \"100.00\", \"location\": { \"id\": 1 } }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Event"))
                .andExpect(jsonPath("$.price").value("100.00"));
    }

    @Test
    public void testGetEventById() throws Exception {
        String locationJson = "{ \"slug\": \"test-location\", \"name\": \"Test Location\" }";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isCreated());

        String eventJson = "{ \"title\": \"Test Event\", \"price\": \"50.00\", \"location\": { \"id\": 1 } }";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.price").value("50.00"));
    }

    @Test
    public void testEventNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/events/999")) // Non-existent ID
                .andExpect(status().isNotFound());
    }
}
