package com.example.api.controller;

import com.example.api.model.EventModel;
import com.example.api.service.CurrencyService;
import com.example.api.service.KudagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class EventController {

    private final KudagoService kudagoService;
    private final CurrencyService currencyService;

    public EventController(KudagoService kudagoService, CurrencyService currencyService) {
        this.kudagoService = kudagoService;
        this.currencyService = currencyService;
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
}