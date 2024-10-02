package com.example.api.service;

import com.example.api.model.CategoryModel;
import com.example.api.repository.CategoryRepository;
import com.example.api.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    private CategoryRepository categoryRepository;
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void getAllCategories_ReturnsListOfCategories() {
        CategoryModel category1 = new CategoryModel();
        category1.setId(1L);
        category1.setSlug("slug1");
        category1.setName("Category 1");

        CategoryModel category2 = new CategoryModel();
        category2.setId(2L);
        category2.setSlug("slug2");
        category2.setName("Category 2");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        assertEquals(2, categoryService.getAllCategories().size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_ReturnsCategory_WhenExists() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<CategoryModel> result = categoryService.getCategoryById(1L);
        assertTrue(result.isPresent());
        assertEquals("Category 1", result.get().getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void getCategoryById_ReturnsEmpty_WhenNotExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CategoryModel> result = categoryService.getCategoryById(1L);
        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void createCategory_SavesNewCategory() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");

        when(categoryRepository.save(category)).thenReturn(category);

        CategoryModel savedCategory = categoryService.createCategory(category);
        assertNotNull(savedCategory);
        assertEquals(1L, savedCategory.getId());
        assertEquals("Category 1", savedCategory.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void updateCategory_ReturnsUpdatedCategory_WhenExists() {
        CategoryModel existingCategory = new CategoryModel();
        existingCategory.setId(1L);
        existingCategory.setSlug("slug1");
        existingCategory.setName("Category 1");

        CategoryModel updatedCategory = new CategoryModel();
        updatedCategory.setSlug("updatedSlug");
        updatedCategory.setName("Updated Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(CategoryModel.class))).thenReturn(existingCategory);

        CategoryModel result = categoryService.updateCategory(1L, updatedCategory);

        assertEquals("Updated Category", result.getName());
        assertEquals("updatedSlug", result.getSlug());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(existingCategory); // Проверка, что save был вызван
    }


    @Test
    void updateCategory_ThrowsException_WhenNotExists() {
        CategoryModel updatedCategory = new CategoryModel();
        updatedCategory.setId(1L);
        updatedCategory.setSlug("updatedSlug");
        updatedCategory.setName("Updated Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(1L, updatedCategory);
        });

        assertEquals("Category not found with id 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void deleteCategory_CallsDeleteOnRepository_WhenCategoryExists() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new CategoryModel())); // Возвращаем существующую категорию

        categoryService.deleteById(categoryId);

        verify(categoryRepository, times(1)).delete(categoryId);
    }
}

