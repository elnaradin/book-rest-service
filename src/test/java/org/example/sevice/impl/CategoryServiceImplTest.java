package org.example.sevice.impl;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.sevice.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {
    private CategoryService categoryService;
    private CategoryRepository categoryRepository;


    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void testGetAll() {
        List<Category> categoryList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Category category = new Category();
            category.setId((long) i);
            category.setName("Category #" + i);
            categoryList.add(category);
        }
        when(categoryRepository.findAll()).thenReturn(categoryList);
        List<Category> all = categoryService.findAll();
        assertEquals(categoryList, all);
    }

    @Test
    void testDeleteById() {
        long id = 1;
        categoryService.deleteById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdate() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Leo Tolstoy");
        categoryService.update(category);
        verify(categoryRepository, times(1)).update(category);
    }

    @Test
    void testAdd() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Leo Tolstoy");

        categoryService.add(category);
        verify(categoryRepository, times(1)).save(category);
    }
}