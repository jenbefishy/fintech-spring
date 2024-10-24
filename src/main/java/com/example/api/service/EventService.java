package com.example.api.service;

import com.example.api.model.EventModel;

import java.util.List;

public interface EventService {
    List<EventModel> findAllEvents();

    EventModel findEventById(Long id);

    EventModel createEvent(EventModel eventModel);

    EventModel updateEvent(Long id, EventModel eventModel);

    boolean deleteEvent(Long id);

    boolean eventExists(Long id);
}
