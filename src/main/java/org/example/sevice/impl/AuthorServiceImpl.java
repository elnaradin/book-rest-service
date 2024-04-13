package org.example.sevice.impl;

import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.example.sevice.AuthorService;

import java.util.List;

public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void add(Author author) {
        authorRepository.save(author);
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void update(Author item) {
        authorRepository.update(item);
    }
}
