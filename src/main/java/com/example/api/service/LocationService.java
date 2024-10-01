package com.example.api.service;

import com.example.api.model.LocationModel;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    void createLocation(LocationModel location);
    Optional<LocationModel> getLocationById(Long id);
    List<LocationModel> getAllLocations();
    void deleteLocation(Long id);
    void updateLocation(LocationModel location);
}
