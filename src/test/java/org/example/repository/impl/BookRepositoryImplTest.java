package org.example.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

class BookRepositoryImplTest extends BaseIT {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;
    private final String[] names = {"War and Peace", "Evgeniy Onegin", "Crime and Punishment"};

    @BeforeEach
    void setUp() {
        super.setUp();
        bookRepository = new BookRepositoryImpl(cm, rsMapper);
        authorRepository = new AuthorRepositoryImpl(cm, rsMapper);
        categoryRepository = new CategoryRepositoryImpl(cm, rsMapper);
        Arrays.stream(names).forEach(n -> {
            Book book = new Book();
            book.setTitle(n);
            bookRepository.save(book);
        });
    }

    @Test
    void findAll() {
        List<Book> books = bookRepository.findAll();
        assertThat(books, hasSize(names.length));
    }

    @Test
    void findById() {
        List<Book> categories = bookRepository.findAll();
        Book bookToFind = categories.get(0);
        Optional<Book> bookLeo = bookRepository.findById(bookToFind.getId());
        assertTrue(bookLeo.isPresent());
        assertEquals(names[0], bookLeo.get().getTitle());
    }

    @Test
    void testDeleteById() {
        long idToDelete;
        List<Book> categories = bookRepository.findAll();
        idToDelete = categories.get(0).getId();
        assertThat(categories, hasSize(names.length));
        bookRepository.deleteById(idToDelete);
        categories = bookRepository.findAll();
        assertThat(categories, hasSize(names.length - 1));
        Optional<Book> bookLeo = bookRepository.findById(idToDelete);
        assertTrue(bookLeo.isEmpty());
    }

    @Test
    void testSave() {
        Author author = new Author();
        author.setName("Leo");
        authorRepository.save(author);
        Author authorSaved = authorRepository.findAll()
                .stream()
                .filter(a -> author.getName().equals(a.getName()))
                .findFirst()
                .orElseThrow();
        Category category = new Category();
        category.setName("romance");
        categoryRepository.save(category);
        Category categorySaved = categoryRepository.findAll().stream()
                .filter(c -> category.getName().equals(c.getName()))
                .findFirst()
                .orElseThrow();
        Book book = new Book();
        book.setTitle("Fantasy");
        book.setAuthor(authorSaved);
        book.setCategories(List.of(categorySaved));
        bookRepository.save(book);
        List<Book> books = bookRepository.findAll();
        assertThat(books, hasSize(names.length + 1));
        Book bookSaved = books
                .stream()
                .filter(a ->
                        book.getTitle().equals(a.getTitle()))
                .findFirst()
                .orElseThrow();
        assertNotNull(bookSaved.getAuthor());
        assertNotNull(bookSaved.getCategories());
        assertEquals(author.getName(), bookSaved.getAuthor().getName());
        assertEquals(category.getName(), bookSaved.getCategories().get(0).getName());
    }

    @Test
    void testUpdateBookWithAuthorAndCategories() {
        Author author = new Author();
        author.setName("Leo");
        authorRepository.save(author);
        Author authorSaved = authorRepository.findAll()
                .stream()
                .filter(a -> author.getName().equals(a.getName()))
                .findFirst()
                .orElseThrow();
        Category category = new Category();
        category.setName("romance");
        categoryRepository.save(category);
        Category categorySaved = categoryRepository.findAll().stream()
                .filter(c -> category.getName().equals(c.getName()))
                .findFirst()
                .orElseThrow();
        String newName = "Anna Karenina";
        Book bookOld = bookRepository.findAll()
                .stream().filter(c -> names[0].equals(c.getTitle()))
                .findFirst().orElseThrow();
        bookOld.setTitle(newName);
        bookOld.setAuthor(authorSaved);
        bookOld.setCategories(List.of(categorySaved));
        bookRepository.update(bookOld);
        Book bookSaved = bookRepository.findAll()
                .stream().filter(c -> newName.equals(c.getTitle()))
                .findFirst().orElseThrow();
        assertEquals(newName, bookSaved.getTitle());
        assertNotNull(bookSaved.getAuthor());
        assertNotNull(bookSaved.getCategories());
        assertEquals(author.getName(), bookSaved.getAuthor().getName());
        assertEquals(category.getName(), bookSaved.getCategories().get(0).getName());
    }

    @Test
    void testUpdateBookWithAuthor() {
        Author author = new Author();
        author.setName("Leo");
        authorRepository.save(author);
        Author authorSaved = authorRepository.findAll()
                .stream()
                .filter(a -> author.getName().equals(a.getName()))
                .findFirst()
                .orElseThrow();

        String newName = "Anna Karenina";
        Book bookOld = bookRepository.findAll()
                .stream().filter(c -> names[0].equals(c.getTitle()))
                .findFirst().orElseThrow();
        bookOld.setTitle(newName);
        bookOld.setAuthor(authorSaved);

        bookRepository.update(bookOld);
        Book bookSaved = bookRepository.findAll()
                .stream().filter(c -> newName.equals(c.getTitle()))
                .findFirst().orElseThrow();
        assertEquals(newName, bookSaved.getTitle());
        assertNotNull(bookSaved.getAuthor());
        assertNotNull(bookSaved.getCategories());
        assertEquals(author.getName(), bookSaved.getAuthor().getName());
        assertTrue(CollectionUtils.isEmpty(bookSaved.getCategories()));
    }

    @Test
    void testUpdateBookWithCategories() {
        Category category = new Category();
        category.setName("romance");
        categoryRepository.save(category);
        Category categorySaved = categoryRepository.findAll().stream()
                .filter(c -> category.getName().equals(c.getName()))
                .findFirst()
                .orElseThrow();
        String newName = "Anna Karenina";
        Book bookOld = bookRepository.findAll()
                .stream().filter(c -> names[0].equals(c.getTitle()))
                .findFirst().orElseThrow();
        bookOld.setTitle(newName);
        bookOld.setCategories(List.of(categorySaved));
        bookRepository.update(bookOld);
        Book bookSaved = bookRepository.findAll()
                .stream().filter(c -> newName.equals(c.getTitle()))
                .findFirst().orElseThrow();
        assertEquals(newName, bookSaved.getTitle());
        assertNull(bookSaved.getAuthor());
        assertNotNull(bookSaved.getCategories());
        assertNull(bookSaved.getAuthor());
        assertEquals(category.getName(), bookSaved.getCategories().get(0).getName());
    }

}