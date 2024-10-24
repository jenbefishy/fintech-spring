package com.example.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import com.example.api.model.LocationModel;
import com.example.api.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class LocationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
    }

    @Test
    public void testGetAllLocations() throws Exception {
        LocationModel location1 = new LocationModel(1L, "slug1", "Location 1", null);
        LocationModel location2 = new LocationModel(2L, "slug2", "Location 2", null);

        when(locationService.getAllLocations()).thenReturn(Arrays.asList(location1, location2));

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].slug").value("slug1"))
                .andExpect(jsonPath("$[0].name").value("Location 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].slug").value("slug2"))
                .andExpect(jsonPath("$[1].name").value("Location 2"));
    }

    @Test
    public void testGetLocationFound() throws Exception {
        LocationModel location = new LocationModel(1L, "slug1", "Location 1", null);

        when(locationService.getLocationById(anyLong())).thenReturn(Optional.of(location));

        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.slug").value("slug1"))
                .andExpect(jsonPath("$.name").value("Location 1"));
    }


    @Test
    public void testCreateLocation() throws Exception {
        LocationModel location = new LocationModel(null, "slug1", "New Location", null);

        when(locationService.createLocation(any(LocationModel.class))).thenReturn(location);

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\":\"slug1\", \"name\":\"New Location\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.slug").value("slug1"))
                .andExpect(jsonPath("$.name").value("New Location"));
    }

    @Test
    public void testUpdateLocation() throws Exception {
        LocationModel location = new LocationModel(1L, "slug1", "Updated Location", null);

        when(locationService.updateLocation(any(LocationModel.class))).thenReturn(location);

        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\":\"slug1\", \"name\":\"Updated Location\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.slug").value("slug1"))
                .andExpect(jsonPath("$.name").value("Updated Location"));
    }

    @Test
    public void testDeleteLocation() throws Exception {
        doNothing().when(locationService).deleteLocation(anyLong());

        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isOk());

        verify(locationService, times(1)).deleteLocation(1L);
    }
}
