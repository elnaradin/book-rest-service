package org.example.repository;

import org.example.model.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findById(List<Long> ids);

}
