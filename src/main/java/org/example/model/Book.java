package org.example.model;

import java.util.List;

public class Book {
    private Long id;
    private String title;
    private Author author;

    private List<Category> categories;

    public Book(Long id, String title, Author author, List<Category> categories) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.categories = categories;
    }

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}

