package org.example.repository.impl;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

class CategoryRepositoryImplTest extends BaseIT {
    private CategoryRepository categoryRepository;
    private final String[] names = {"Comedy", "Drama", "Sci-Fi"};


    @BeforeEach
    void setUp() {
        super.setUp();
        categoryRepository = new CategoryRepositoryImpl(cm, rsMapper);

        Arrays.stream(names).forEach(n -> {
            Category category = new Category();
            category.setName(n);
            categoryRepository.save(category);
        });
    }

    @Test
    void testFindAll() {
        List<Category> categorys = categoryRepository.findAll();
        assertThat(categorys, hasSize(names.length));
    }

    @Test
    void testFindById() {
        List<Category> categories = categoryRepository.findAll();
        Category categoryToFind = categories.get(0);
        Optional<Category> categoryLeo = categoryRepository.findById(categoryToFind.getId());
        assertTrue(categoryLeo.isPresent());
        assertEquals(names[0], categoryLeo.get().getName());
    }

    @Test
    void tesDeleteById() {
        long idToDelete;
        List<Category> categories = categoryRepository.findAll();
        idToDelete = categories.get(0).getId();
        assertThat(categories, hasSize(names.length));
        categoryRepository.deleteById(idToDelete);
        categories = categoryRepository.findAll();
        assertThat(categories, hasSize(names.length - 1));
        Optional<Category> categoryLeo = categoryRepository.findById(idToDelete);
        assertTrue(categoryLeo.isEmpty());
    }

    @Test
    void testSave() {
        Category category = new Category();
        category.setName("Fantasy");
        categoryRepository.save(category);
        List<Category> categorys = categoryRepository.findAll();
        assertThat(categorys, hasSize(names.length + 1));
        assertTrue(categorys.stream().anyMatch(a -> category.getName().equals(a.getName())));
    }

    @Test
    void testUpdate() {
        String newName = "Romantic Comedy";
        Category categoryOld = categoryRepository.findAll()
                .stream().filter(c -> names[0].equals(c.getName()))
                .findFirst().orElseThrow();
        categoryOld.setName(newName);
        categoryRepository.update(categoryOld);
        Category categoryNew = categoryRepository.findAll()
                .stream().filter(c -> newName.equals(c.getName()))
                .findFirst().orElseThrow();
        assertEquals(newName, categoryNew.getName());
    }

    @Test
    void testFindListById() {
        List<Category> categories = categoryRepository.findAll();
        List<Long> categoriesToFind = categories.stream().limit(3).map(Category::getId).toList();
        List<Category> categoryList = categoryRepository.findById(categoriesToFind);
        assertEquals(3, categoryList.size());
    }
}