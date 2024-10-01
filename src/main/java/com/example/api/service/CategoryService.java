package com.example.api.service;

import com.example.api.model.CategoryModel;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public List<CategoryModel> getAllCategories();
    public Optional<CategoryModel> getCategoryById(Long id);
    public CategoryModel createCategory(CategoryModel category);
    public void deleteCategory(Long id);
    public void updateCategory(CategoryModel category);
}
