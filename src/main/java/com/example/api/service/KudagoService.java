package com.example.api.service;
import com.example.api.model.CategoryModel;
import com.example.api.model.LocationModel;

import java.util.List;

public interface KudagoService {
    public List<LocationModel> fetchLocations();
    public List<CategoryModel> fetchCategories();
}
