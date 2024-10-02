package com.example.api.service;

import com.example.api.model.LocationModel;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    LocationModel createLocation(LocationModel location);
    Optional<LocationModel> getLocationById(Long id);
    List<LocationModel> getAllLocations();
    void deleteLocation(Long id);
    LocationModel updateLocation(LocationModel location);
}
