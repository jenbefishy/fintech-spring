package com.example.api.service;

import com.example.api.model.CategoryModel;
import com.example.api.model.EventModel;
import com.example.api.model.EventResponse;
import com.example.api.model.LocationModel;
import com.example.api.service.impl.KudagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class KudagoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private KudagoServiceImpl kudagoService;
    private Semaphore semaphore;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        semaphore = new Semaphore(5);
        kudagoService = new KudagoServiceImpl(restTemplate, 5);
    }

    @Test
    void testRateLimiting() throws InterruptedException {
        int numRequests = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numRequests);

        for (int i = 0; i < numRequests; i++) {
            executor.submit(() -> {
                List<LocationModel> locations = kudagoService.fetchLocations();
                assertEquals(0, locations.size());
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }


    @Test
    void testFetchEvents_Success() {
        EventModel event1 = new EventModel(1L, "Title1", "Price 1", null);
        EventModel event2 = new EventModel(2L, "Title2", "Price 2", null);
        EventResponse mockResponse = new EventResponse(Arrays.asList(event1, event2));

        when(restTemplate.getForObject(anyString(), eq(EventResponse.class))).thenReturn(mockResponse);

        List<EventModel> events = kudagoService.fetchEvents(1620000000L, 1620003600L);

        assertEquals(2, events.size());
        assertEquals("Title1", events.get(0).getTitle());
        assertEquals("Title2", events.get(1).getTitle());
    }

    @Test
    void testFetchEvents_NoEvents() {
        when(restTemplate.getForObject(anyString(), eq(EventResponse.class))).thenReturn(new EventResponse(null));

        List<EventModel> events = kudagoService.fetchEvents(1620000000L, 1620003600L);

        assertTrue(events.isEmpty());
    }

}
