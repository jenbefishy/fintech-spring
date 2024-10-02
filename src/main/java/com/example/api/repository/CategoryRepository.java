package com.example.api.repository;

import com.example.api.model.CategoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    public CategoryModel save(CategoryModel category);
    public Optional<CategoryModel> findById(Long id);
    public List<CategoryModel> findAll();
    public void delete(Long id);
    void update(CategoryModel category);
}
