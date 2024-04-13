package org.example.repository.impl;

import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;


class AuthorRepositoryImplTest extends BaseIT {
    private AuthorRepository authorRepository;
    private final String[] names = {"Leo Tolstoy", "Fyodor Dostoyevsky", "Alexander Pushkin"};

    @BeforeEach
    void setUp() {
        super.setUp();
        authorRepository = new AuthorRepositoryImpl(cm, rsMapper);
        Arrays.stream(names).forEach(n -> {
            Author author = new Author();
            author.setName(n);
            authorRepository.save(author);
        });

    }

    @Test
    void findAll() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors, hasSize(names.length));
    }

    @Test
    void testFindById() {
        Author authorToFind = authorRepository.findAll().get(0);
        Optional<Author> authorLeo = authorRepository.findById(authorToFind.getId());
        assertTrue(authorLeo.isPresent());
        assertEquals(names[0], authorLeo.get().getName());
    }

    @Test
    void testDeleteById() {

        List<Author> authors = authorRepository.findAll();
        assertThat(authors, hasSize(names.length));
        Author authorToDelete = authors.stream()
                .filter(a -> names[0].equals(a.getName()))
                .findFirst()
                .orElseThrow();
        authorRepository.deleteById(authorToDelete.getId());
        Optional<Author> authorLeo = authorRepository.findById(authorToDelete.getId());
        assertTrue(authorLeo.isEmpty());
        List<Author> authorsNew = authorRepository.findAll();
        assertThat(authorsNew, hasSize(names.length - 1));
    }

    @Test
    void testSave() {
        Author author = new Author();
        author.setName("Nikolay Gogol");
        authorRepository.save(author);
        List<Author> authors = authorRepository.findAll();
        assertThat(authors, hasSize(names.length + 1));
        assertTrue(authors.stream().anyMatch(a -> author.getName().equals(a.getName())));
    }

    @Test
    void testUpdate() {
        String newName = "Leo Nikolaevich Tolstoy";
        Author authorToFind = authorRepository.findAll()
                .stream()
                .filter(a -> names[0].equals(a.getName()))
                .findFirst()
                .orElseThrow();
        Author authorOld = authorRepository.findById(authorToFind.getId()).orElseThrow();
        authorOld.setName(newName);
        authorRepository.update(authorOld);
        Author authorNew = authorRepository.findById(authorToFind.getId()).orElseThrow();
        assertEquals(newName, authorNew.getName());
    }
}