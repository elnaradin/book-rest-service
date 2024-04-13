package org.example.repository.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapperImpl implements ResultSetMapper {
    @Override
    public List<Book> map2BookList(ResultSet rs) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            Book book = map2Book(rs);
            books.add(book);
        }
        return books;

    }

    @Override
    public Book map2Book(ResultSet rs) throws SQLException {
        long i = rs.getLong("id");
        String title = rs.getString("title");
        Book book = new Book();
        book.setId(i);
        book.setTitle(title);

        Long authorId = rs.getLong("author_id");
        if (!rs.wasNull()) {
            String authorName = rs.getString("author_name");
            Author author = new Author();
            author.setId(authorId);
            author.setName(authorName);
            book.setAuthor(author);
        }
        return book;
    }

    @Override
    public List<Author> map2AuthorList(ResultSet rs) throws SQLException {
        List<Author> authors = new ArrayList<>();
        while (rs.next()) {
            Author author = map2Author(rs);
            authors.add(author);
        }
        return authors;
    }

    @Override
    public Author map2Author(ResultSet rs) throws SQLException {
        long i = rs.getLong("id");
        String name = rs.getString("name");
        Author author = new Author();
        author.setId(i);
        author.setName(name);
        return author;
    }

    @Override
    public List<Category> map2CategoryList(ResultSet rs) throws SQLException {
        List<Category> categories = new ArrayList<>();
        while (rs.next()) {
            Category category = map2Category(rs);
            categories.add(category);
        }
        return categories;
    }

    @Override
    public Category map2Category(ResultSet rs) throws SQLException {
        long i = rs.getLong("id");
        String name = rs.getString("category_name");
        Category category = new Category();
        category.setId(i);
        category.setName(name);
        return category;
    }
}
