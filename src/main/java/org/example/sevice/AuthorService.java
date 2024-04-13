package org.example.sevice;

import org.example.model.Author;

import java.util.List;

public interface AuthorService {
    void add(Author item);

    List<Author> getAll();

    void deleteById(long id);

    void update(Author item);
}
