package com.example.api.service.impl;

import com.example.api.model.CategoryModel;
import com.example.api.model.EventModel;
import com.example.api.model.EventResponse;
import com.example.api.model.LocationModel;
import com.example.api.service.KudagoService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import java.util.concurrent.Semaphore;

@Service
public class KudagoServiceImpl implements KudagoService {

    @Value("${external.api.locations.url}")
    String locationsUrl;

    @Value("${external.api.categories.url}")
    String categoriesUrl;

    @Value("${external.api.events.url}")
    String eventsUrl;

    private final RestTemplate restTemplate;
    private final Semaphore semaphore;


    public KudagoServiceImpl(RestTemplate restTemplate, @Value("${external.api.kudago.max-concurrent-requests}") int maxConcurrentRequests) {
        this.restTemplate = restTemplate;
        this.semaphore = new Semaphore(maxConcurrentRequests);
    }

    @Override
    public List<LocationModel> fetchLocations() {
        try {
            semaphore.acquire();
            LocationModel[] locations = restTemplate.getForObject(locationsUrl, LocationModel[].class);
            return locations != null ? Arrays.asList(locations) : List.of();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while fetching locations", e);
        } finally {
            semaphore.release();
        }
    }

    @Override
    public List<CategoryModel> fetchCategories() {
        try {
            semaphore.acquire();
            CategoryModel[] categories = restTemplate.getForObject(categoriesUrl, CategoryModel[].class);
            return categories != null ? Arrays.asList(categories) : List.of();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while fetching categories", e);
        } finally {
            semaphore.release();
        }
    }

    @Override
    public List<EventModel> fetchEvents(Long startTimestamp, Long endTimestamp) {
        try {
            semaphore.acquire();
            String url = String.format(eventsUrl + "?fields=id,title,price&order_by=-price&text_format=plain" +
                            "&actual_since=%d&actual_until=%d&page_size=100",
                    startTimestamp, endTimestamp);
            EventResponse response = restTemplate.getForObject(url, EventResponse.class);
            if (response != null && response.getResults() != null) {
                return response.getResults();
            }
            return List.of();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while fetching events", e);
        } finally {
            semaphore.release();
        }
    }
}

