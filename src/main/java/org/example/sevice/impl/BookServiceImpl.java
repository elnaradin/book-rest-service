package org.example.sevice.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.CategoryRepository;
import org.example.sevice.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;


    public BookServiceImpl(BookRepository bookRepository,
                           CategoryRepository categoryRepository,
                           AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;

        this.authorRepository = authorRepository;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void update(Book item, Long authorId, List<Long> categoryIds) {
        Author author;
        List<Category> categories;
        if (authorId != null) {
            author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new RuntimeException("Author does not exist"));
            item.setAuthor(author);
        }
        if (CollectionUtils.isNotEmpty(categoryIds)) {
            categories = categoryRepository.findById(categoryIds);
            item.setCategories(categories);
        }
        bookRepository.update(item);
    }

    @Override
    public void add(Book item, Long authorId, List<Long> categoryIds) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        List<Category> categories = categoryRepository.findById(categoryIds);
        item.setAuthor(author);
        item.setCategories(categories);
        bookRepository.save(item);
    }

}