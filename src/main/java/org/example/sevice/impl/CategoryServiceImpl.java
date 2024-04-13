package org.example.sevice.impl;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.sevice.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    @Override
    public void add(Category item) {
        categoryRepository.save(item);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void update(Category item) {
        categoryRepository.update(item);
    }
}
