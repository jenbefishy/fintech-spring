package com.example.api.service.impl;

import com.example.api.model.CategoryModel;
import com.example.api.model.LocationModel;
import com.example.api.service.KudagoService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class KudagoServiceImpl implements KudagoService {

    private final RestTemplate restTemplate;

    public KudagoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<LocationModel> fetchLocations() {
        String url = "https://kudago.com/public-api/v1.4/locations/";
        LocationModel[] locations = restTemplate.getForObject(url, LocationModel[].class);
        return locations != null ? Arrays.asList(locations) : List.of();
    }

    public List<CategoryModel> fetchCategories() {
        String url = "https://kudago.com/public-api/v1.4/place-categories/";
        CategoryModel[] categories = restTemplate.getForObject(url, CategoryModel[].class);
        return categories != null ? Arrays.asList(categories) : List.of();
    }
}
