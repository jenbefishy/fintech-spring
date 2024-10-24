package com.example.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
public class CategoryModel {

    @Id
    private Long id;
    private String slug;
    private String name;

    public CategoryModel() {
    }

}
