package org.example.sevice.impl;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.CategoryRepository;
import org.example.sevice.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceImplTest {
    private BookService bookService;
    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        authorRepository = mock(AuthorRepository.class);
        bookService = new BookServiceImpl(bookRepository, categoryRepository, authorRepository);
    }

    @Test
    void testGetAll() {
        List<Book> bookList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Book book = new Book();
            book.setId((long) i);
            book.setTitle("Book #" + i);
            bookList.add(book);
        }
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> all = bookService.getAll();
        assertEquals(bookList, all);
    }

    @Test
    void testDeleteById() {
        long id = 1;
        bookService.deleteById(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdate() {
        long authorId = 1;
        List<Long> categoryIds = List.of(1L, 2L, 3L);
        List<Category> categoryList = categoryIds
                .stream()
                .map(id -> new Category(id, "category " + id, List.of()))
                .toList();

        Book book = new Book();
        book.setId(1L);
        book.setTitle("War and Peace");
        when(authorRepository.findById(book.getId()))
                .thenReturn(Optional.of(new Author(authorId, "Leo Tolstoy")));
        when(categoryRepository.findById(categoryIds))
                .thenReturn(categoryList);
        bookService.update(book, authorId, categoryIds);
        verify(bookRepository, times(1)).update(book);
    }

    @Test
    void testAdd() {
        long authorId = 1;
        List<Long> categoryIds = List.of(1L, 2L, 3L);
        List<Category> categoryList = categoryIds
                .stream()
                .map(id -> new Category(id, "category " + id, List.of()))
                .toList();

        Book book = new Book();
        book.setId(1L);
        book.setTitle("War and Peace");
        when(authorRepository.findById(book.getId()))
                .thenReturn(Optional.of(new Author(authorId, "Leo Tolstoy")));
        when(categoryRepository.findById(categoryIds))
                .thenReturn(categoryList);
        bookService.add(book, authorId, categoryIds);
        verify(bookRepository, times(1)).save(book);
    }
}