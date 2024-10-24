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

    public LocationServiceImpl(LocationRepository placeRepository) {
        this.locationRepository = placeRepository;
    }

    @Override
    public LocationModel createLocation(LocationModel location) {
        return locationRepository.save(location);
    }

    @Override
    public Optional<LocationModel> getLocationById(Long id) {
        return Optional.ofNullable(locationRepository.findByIdWithEvents(id));
    }

    @Override
    public List<LocationModel> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public LocationModel updateLocation(LocationModel location) {
        if (location.getId() == null) {
            throw new IllegalArgumentException("Location ID cannot be null for update.");
        }
        return locationRepository.save(location);
    }
}
