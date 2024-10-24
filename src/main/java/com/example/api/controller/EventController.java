package com.example.api.controller;

import com.example.api.exception.BadRequestException;
import com.example.api.exception.NotFoundException;
import com.example.api.model.EventModel;
import com.example.api.repository.EventRepository;
import com.example.api.service.CurrencyService;
import com.example.api.service.EventService;
import com.example.api.service.KudagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Validated
@RestController
public class EventController {

    private final KudagoService kudagoService;
    private final CurrencyService currencyService;
    private final EventService eventService;

    public EventController(KudagoService kudagoService, CurrencyService currencyService, EventService eventService) {
        this.kudagoService = kudagoService;
        this.currencyService = currencyService;
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public Mono<ResponseEntity<List<EventModel>>> getEvents(
            @RequestParam double budget,
            @RequestParam String currency,
            @RequestParam(required = false) Long dateFrom,
            @RequestParam(required = false) Long dateTo) {

        LocalDateTime now = LocalDateTime.now();
        if (dateFrom == null) {
            dateFrom = now.with(DayOfWeek.MONDAY).toEpochSecond(ZoneOffset.UTC);
        }
        if (dateTo == null) {
            dateTo = now.with(DayOfWeek.SUNDAY).toEpochSecond(ZoneOffset.UTC);
        }

        Long finalDateFrom = dateFrom;
        Long finalDateTo = dateTo;

        Mono<List<EventModel>> eventsMono = Mono.fromCallable(() ->
                kudagoService.fetchEvents(finalDateFrom, finalDateTo));

        Mono<Double> convertedBudgetMono = currencyService.convertCurrency(budget, currency);

        return Mono.zip(eventsMono, convertedBudgetMono)
                .map(tuple -> {
                    List<EventModel> events = tuple.getT1();
                    double convertedBudget = tuple.getT2();
                    List<EventModel> filteredEvents = events.stream()
                            .filter(event -> event.extractFirstPrice() <= convertedBudget)
                            .toList();
                    return ResponseEntity.ok(filteredEvents);
                });
    }

    @GetMapping
    public List<EventModel> getAllEvents() {
        return eventService.findAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable Long id) {
        EventModel event = eventService.findEventById(id);
        if (event == null) {
            throw new NotFoundException("Event not found with ID: " + id);
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventModel> createEvent(@Valid @RequestBody EventModel eventModel) {
        if (eventModel.getLocation() == null || eventModel.getLocation().getId() == null) {
            throw new BadRequestException("Associated location not found.");
        }
        EventModel createdEvent = eventService.createEvent(eventModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventModel> updateEvent(@PathVariable Long id, @Valid @RequestBody EventModel eventModel) {
        if (!eventService.eventExists(id)) {
            throw new NotFoundException("Event not found with ID: " + id);
        }
        EventModel updatedEvent = eventService.updateEvent(id, eventModel);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventService.deleteEvent(id)) {
            throw new NotFoundException("Event not found with ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}