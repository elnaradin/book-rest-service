package org.example.sevice;

import org.example.model.Book;

import java.util.List;

public interface BookService {

    void add(Book item, Long authorId, List<Long> categoryIds);

    List<Book> getAll();

    void deleteById(long id);

    void update(Book item, Long authorId, List<Long> categoryIds);

}
