package com.example.api.service.impl;

import com.example.api.model.LocationModel;
import com.example.api.repository.LocationRepository;
import com.example.api.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void createLocation(LocationModel location) {
        locationRepository.save(location);
    }

    @Override
    public Optional<LocationModel> getLocationById(Long id) {
        return Optional.ofNullable(locationRepository.findById(id));
    }

    @Override
    public List<LocationModel> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.delete(id);
    }

    @Override
    public void updateLocation(LocationModel location) {
        if (location.getId() == null) {
            throw new IllegalArgumentException("Location ID cannot be null for update.");
        }
        locationRepository.update(location);
    }
}
