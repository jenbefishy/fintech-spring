package com.example.api.repository.impl;

import com.example.api.model.LocationModel;
import com.example.api.repository.LocationRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryLocationRepository implements LocationRepository {
    private final ConcurrentHashMap<Long, LocationModel> locationMap = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public void save(LocationModel location) {
        if (location.getId() == null) {
            location.setId(currentId.getAndIncrement());
        }
        locationMap.put(location.getId(), location);
    }

    @Override
    public LocationModel findById(Long id) {
        return locationMap.get(id);
    }

    @Override
    public List<LocationModel> findAll() {
        return new ArrayList<>(locationMap.values());
    }

    @Override
    public void delete(Long id) {
        locationMap.remove(id);
    }

    @Override
    public void update(LocationModel location) {
        if (location.getId() != null && locationMap.containsKey(location.getId())) {
            locationMap.put(location.getId(), location);
        } else {
            throw new IllegalArgumentException("Location with ID " + location.getId() + " does not exist.");
        }
    }
}
