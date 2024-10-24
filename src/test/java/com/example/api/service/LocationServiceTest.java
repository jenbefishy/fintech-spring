package com.example.api.service;

import com.example.api.model.LocationModel;
import com.example.api.repository.LocationRepository;
import com.example.api.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLocation() {
        LocationModel location = new LocationModel(1L, "ekb", "Екатеринбург", null);

        locationService.createLocation(location);

        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void testGetLocationById() {
        LocationModel location = new LocationModel(1L, "ekb", "Екатеринбург", null);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Optional<LocationModel> found = locationService.getLocationById(1L);

        assertTrue(found.isPresent(), "Location should be present");
        assertEquals("ekb", found.get().getSlug());
        assertEquals("Екатеринбург", found.get().getName());
        verify(locationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllLocations() {
        LocationModel location1 = new LocationModel(1L, "ekb", "Екатеринбург", null);
        LocationModel location2 = new LocationModel(2L, "kzn", "Казань", null);
        when(locationRepository.findAll()).thenReturn(List.of(location1, location2));

        List<LocationModel> locations = locationService.getAllLocations();

        assertEquals(2, locations.size());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void testDeleteLocation() {
        Long id = 1L;

        locationService.deleteLocation(id);

        verify(locationRepository, times(1)).deleteById(id);
    }

}
