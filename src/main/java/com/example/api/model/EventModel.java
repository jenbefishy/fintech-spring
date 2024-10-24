package com.example.api.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event_model")
@jakarta.persistence.Entity
public class EventModel {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Price cannot be empty")
    private String price;

    @jakarta.persistence.ManyToOne
    @JoinColumn(name = "location_id")
    private LocationModel location;

    public double extractFirstPrice() {
        if (price == null || price.isEmpty()) {
            return 0;
        }

        String numberStr = price.replaceAll("[^0-9]+", " ").trim().split(" ")[0];

        try {
            return Double.parseDouble(numberStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
