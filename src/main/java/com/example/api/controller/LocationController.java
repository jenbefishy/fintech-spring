package com.example.api.controller;

import com.example.api.annotation.LogExecutionTime;
import com.example.api.model.LocationModel;
import com.example.api.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@LogExecutionTime
@RequestMapping("/api/v1/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<LocationModel> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public Optional<LocationModel> getLocation(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @PostMapping
    public LocationModel createLocation(@RequestBody LocationModel location) {
        locationService.createLocation(location);
        return location;
    }

    @PutMapping("/{id}")
    public LocationModel updateLocation(@PathVariable Long id, @RequestBody LocationModel location) {
        location.setId(id);
        locationService.updateLocation(location);
        return location;
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}
