package com.example.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import com.example.api.model.CategoryModel;
import com.example.api.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void testGetAllCategories() throws Exception {
        CategoryModel category1 = new CategoryModel();
        category1.setId(1L);
        category1.setSlug("slug1");
        category1.setName("Category 1");

        CategoryModel category2 = new CategoryModel();
        category2.setId(2L);
        category2.setSlug("slug2");
        category2.setName("Category 2");


        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    @Test
    public void testGetCategoryByIdFound() throws Exception {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");


        when(categoryService.getCategoryById(anyLong())).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/v1/places/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    public void testGetCategoryByIdNotFound() throws Exception {
        when(categoryService.getCategoryById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/places/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCategory() throws Exception {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");

        when(categoryService.createCategory(any(CategoryModel.class))).thenReturn(category);

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Category\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteById(anyLong());

        mockMvc.perform(delete("/api/v1/places/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateCategory() throws Exception {
        CategoryModel category = new CategoryModel();
        category.setId(1L);
        category.setSlug("slug1");
        category.setName("Category 1");

        when(categoryService.updateCategory(anyLong(), any(CategoryModel.class))).thenReturn(category);

        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Category\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Category"));
    }
}
