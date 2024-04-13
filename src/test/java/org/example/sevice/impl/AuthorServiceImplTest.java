package org.example.sevice.impl;

import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.example.sevice.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthorServiceImplTest {
    private AuthorService authorService;
    private AuthorRepository authorRepository;


    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    void testGetAll() {
        List<Author> authorList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Author author = new Author();
            author.setId((long) i);
            author.setName("Author #" + i);
            authorList.add(author);
        }
        when(authorRepository.findAll()).thenReturn(authorList);
        List<Author> all = authorService.getAll();
        assertEquals(authorList, all);
    }

    @Test
    void testDeleteById() {
        long id = 1;
        authorService.deleteById(id);
        verify(authorRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdate() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Leo Tolstoy");
        authorService.update(author);
        verify(authorRepository, times(1)).update(author);
    }

    @Test
    void testAdd() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Leo Tolstoy");

        authorService.add(author);
        verify(authorRepository, times(1)).save(author);
    }
}