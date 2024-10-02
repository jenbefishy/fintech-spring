package com.example.api.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.api.model.CategoryModel;
import com.example.api.repository.impl.InMemoryCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class InMemoryCategoryRepositoryTest {

    private InMemoryCategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        categoryRepository = new InMemoryCategoryRepository();
    }

    @Test
    public void testSaveNewCategory() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");

        CategoryModel savedCategory = categoryRepository.save(category);

        assertNotNull(savedCategory.getId());
        assertEquals("Category 1", savedCategory.getName());
        assertEquals(savedCategory, categoryRepository.findById(savedCategory.getId()).orElse(null));
    }

    @Test
    public void testSaveExistingCategory()
    {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");
        CategoryModel savedCategory = categoryRepository.save(category);

        savedCategory.setName("Updated Category");
        categoryRepository.save(savedCategory);

        CategoryModel updatedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
    }

    @Test
    public void testFindByIdExisting() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");
        CategoryModel savedCategory = categoryRepository.save(category);

        Optional<CategoryModel> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertTrue(foundCategory.isPresent());
        assertEquals("Category 1", foundCategory.get().getName());
    }

    @Test
    public void testFindByIdNonExisting() {
        Optional<CategoryModel> foundCategory = categoryRepository.findById(999L);
        assertFalse(foundCategory.isPresent());
    }

    @Test
    public void testFindAll() {
        CategoryModel category1 = new CategoryModel();
        category1.setId(1L);
        category1.setSlug("slug1");
        category1.setName("Category 1");

        CategoryModel category2 = new CategoryModel();
        category2.setId(2L);
        category2.setSlug("slug2");
        category2.setName("Category 2");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<CategoryModel> allCategories = categoryRepository.findAll();

        assertEquals(2, allCategories.size());
        assertTrue(allCategories.contains(category1));
        assertTrue(allCategories.contains(category2));
    }

    @Test
    public void testDeleteExistingCategory() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");
        CategoryModel savedCategory = categoryRepository.save(category);

        categoryRepository.delete(savedCategory.getId());

        assertFalse(categoryRepository.findById(savedCategory.getId()).isPresent());
    }

    @Test
    public void testDeleteNonExistingCategory() {
        assertDoesNotThrow(() -> categoryRepository.delete(999L)); // Не должно вызывать исключения
    }

    @Test
    public void testUpdateExistingCategory() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");
        CategoryModel savedCategory = categoryRepository.save(category);

        savedCategory.setName("Updated Category");
        categoryRepository.update(savedCategory);

        CategoryModel updatedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
    }

    @Test
    public void testUpdateNonExistingCategory() {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");

        assertThrows(IllegalArgumentException.class, () -> categoryRepository.update(category));
    }
}