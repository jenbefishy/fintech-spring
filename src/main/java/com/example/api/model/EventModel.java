package com.example.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class EventModel {
    private String title;
    private String price;

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
