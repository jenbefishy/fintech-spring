package com.example.api.service.impl;

import com.example.api.repository.CategoryRepository;
import com.example.api.model.CategoryModel;
import com.example.api.repository.impl.InMemoryCategoryRepository;
import com.example.api.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<CategoryModel> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public CategoryModel createCategory(CategoryModel category) {
        return categoryRepository.save(category);
    }


    @Override
    public void deleteCategory(Long id) {
        CategoryModel category = categoryRepository.findById(id).orElse(null);
        assert category != null;
        categoryRepository.delete(category.getId());
    }

    @Override
    public void updateCategory(CategoryModel category) {
        if (category.getId() == null) {
            throw new IllegalArgumentException("Category ID cannot be null for update.");
        }
        categoryRepository.update(category);
    }
}
