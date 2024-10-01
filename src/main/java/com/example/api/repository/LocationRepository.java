package com.example.api.repository;

import com.example.api.model.LocationModel;
import java.util.List;

public interface LocationRepository {
    void save(LocationModel location);
    LocationModel findById(Long id);
    List<LocationModel> findAll();
    void delete(Long id);
    void update(LocationModel location);


}
