package com.example.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.api.model.LocationModel;
import com.example.api.repository.impl.InMemoryLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryLocationRepositoryTest {

    private InMemoryLocationRepository locationRepository;

    @BeforeEach
    public void setUp() {
        locationRepository = new InMemoryLocationRepository();
    }

    @Test
    public void testSaveNewLocation() {
        LocationModel location = new LocationModel(null, "slug1", "Location 1");

        locationRepository.save(location);

        assertNotNull(location.getId());
        assertEquals(location, locationRepository.findById(location.getId()));
    }

    @Test
    public void testSaveExistingLocation() {
        LocationModel location = new LocationModel(null, "slug1", "Location 1");
        locationRepository.save(location);

        location.setName("Updated Location");
        locationRepository.save(location);

        LocationModel updatedLocation = locationRepository.findById(location.getId());
        assertNotNull(updatedLocation);
        assertEquals("Updated Location", updatedLocation.getName());
    }

    @Test
    public void testFindByIdExisting() {
        LocationModel location = new LocationModel(null, "slug1", "Location 1");
        locationRepository.save(location);

        LocationModel foundLocation = locationRepository.findById(location.getId());

        assertNotNull(foundLocation);
        assertEquals("Location 1", foundLocation.getName());
    }

    @Test
    public void testFindByIdNonExisting() {
        LocationModel foundLocation = locationRepository.findById(999L);
        assertNull(foundLocation);
    }

    @Test
    public void testFindAll() {
        LocationModel location1 = new LocationModel(null, "slug1", "Location 1");
        LocationModel location2 = new LocationModel(null, "slug2", "Location 2");

        locationRepository.save(location1);
        locationRepository.save(location2);

        List<LocationModel> allLocations = locationRepository.findAll();

        assertEquals(2, allLocations.size());
        assertTrue(allLocations.contains(location1));
        assertTrue(allLocations.contains(location2));
    }

    @Test
    public void testDeleteExistingLocation() {
        LocationModel location = new LocationModel(null, "slug1", "Location 1");
        locationRepository.save(location);

        locationRepository.delete(location.getId());

        assertNull(locationRepository.findById(location.getId()));
    }

    @Test
    public void testDeleteNonExistingLocation() {
        assertDoesNotThrow(() -> locationRepository.delete(999L));
    }

    @Test
    public void testUpdateExistingLocation() {
        LocationModel location = new LocationModel(null, "slug1", "Location 1");
        locationRepository.save(location);

        location.setName("Updated Location");
        locationRepository.update(location);

        LocationModel updatedLocation = locationRepository.findById(location.getId());
        assertNotNull(updatedLocation);
        assertEquals("Updated Location", updatedLocation.getName());
    }

    @Test
    public void testUpdateNonExistingLocation() {
        LocationModel location = new LocationModel(999L, "slug1", "Non-Existing Location");
        assertThrows(IllegalArgumentException.class, () -> locationRepository.update(location));
    }
}
