package com.example.api.service;

import com.example.api.model.CategoryModel;
import com.example.api.repository.CategoryRepository;
import com.example.api.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        CategoryModel category = new CategoryModel(null, "cat1", "Категория 1");

        categoryService.createCategory(category);

        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetCategory() {
        CategoryModel category = new CategoryModel(1L, "cat1", "Категория 1");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<CategoryModel> found = categoryService.getCategoryById(1L);


        assertTrue(found.isPresent(), "Category should be present");
        assertEquals("cat1", found.get().getSlug());
        assertEquals("Категория 1", found.get().getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCategories() {
        CategoryModel category1 = new CategoryModel(1L, "cat1", "Категория 1");
        CategoryModel category2 = new CategoryModel(2L, "cat2", "Категория 2");
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<CategoryModel> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCategory() {
        CategoryModel category = new CategoryModel(1L, "cat1", "Категория 1");

        categoryService.updateCategory(category);

        verify(categoryRepository, times(1)).update(category);
    }
}
