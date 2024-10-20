package com.example.api.service.impl;

import com.example.api.repository.CategoryRepository;
import com.example.api.model.CategoryModel;
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
    public void deleteById(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            categoryRepository.delete(id);
        } else {
            throw new RuntimeException("Category not found with id " + id);
        }
    }

    @Override
    public CategoryModel updateCategory(Long id, CategoryModel updatedCategory) {
        Optional<CategoryModel> existingCategoryOpt = categoryRepository.findById(id);
        if (existingCategoryOpt.isPresent()) {
            CategoryModel existingCategory = existingCategoryOpt.get();
            existingCategory.setSlug(updatedCategory.getSlug());
            existingCategory.setName(updatedCategory.getName());
            return categoryRepository.save(existingCategory);
        } else {
            throw new RuntimeException("Category not found with id " + id);
        }
    }
}
