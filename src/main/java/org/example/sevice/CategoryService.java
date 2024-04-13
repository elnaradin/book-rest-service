package org.example.sevice;

import org.example.model.Category;

import java.util.List;

public interface CategoryService {

    void add(Category item);

    List<Category> findAll();

    void deleteById(long id);

    void update(Category item);
}
