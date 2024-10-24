package com.example.api.service.impl;

import com.example.api.exception.BadRequestException;
import com.example.api.model.EventModel;
import com.example.api.repository.EventRepository;
import com.example.api.repository.LocationRepository;
import com.example.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<EventModel> findAllEvents() {
        return eventRepository.findAll();
    }

    public EventModel findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public EventModel createEvent(EventModel eventModel) {
        if (eventModel.getLocation() != null && !locationRepository.existsById(eventModel.getLocation().getId())) {
            throw new BadRequestException("Associated location does not exist.");
        }
        return eventRepository.save(eventModel);
    }

    public EventModel updateEvent(Long id, EventModel eventModel) {
        if (!eventRepository.existsById(id)) {
            return null;
        }
        eventModel.setId(id);
        return eventRepository.save(eventModel);
    }

    public boolean deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            return false;
        }
        eventRepository.deleteById(id);
        return true;
    }

    public boolean eventExists(Long id) {
        return eventRepository.existsById(id);
    }
}

