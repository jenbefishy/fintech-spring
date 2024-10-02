package com.example.api.repository.impl;

import com.example.api.model.CategoryModel;
import com.example.api.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {
    private final ConcurrentHashMap<Long, CategoryModel> categoryMap = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(1); // Используем AtomicLong для генерации id

    @Override
    public CategoryModel save(CategoryModel category) {
        if (category.getId() == null) {
            category.setId(currentId.getAndIncrement()); // Генерируем id автоматически
        }
        categoryMap.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<CategoryModel> findById(Long id) {
        return Optional.ofNullable(categoryMap.get(id));
    }

    @Override
    public List<CategoryModel> findAll() {
        return new ArrayList<>(categoryMap.values());
    }

    @Override
    public void delete(Long id) {
        categoryMap.remove(id);
    }

    @Override
    public void update(CategoryModel category) {
        if (category.getId() != null && categoryMap.containsKey(category.getId())) {
            categoryMap.put(category.getId(), category);
        } else {
            throw new IllegalArgumentException("Category with ID " + category.getId() + " does not exist.");
        }
    }
}

