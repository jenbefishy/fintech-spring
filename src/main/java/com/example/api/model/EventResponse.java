package com.example.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EventResponse {
    private int count;
    private String next;
    private String previous;
    private List<EventModel> results;

    public EventResponse(List<EventModel> list) {
        this.results = list;
    }
}