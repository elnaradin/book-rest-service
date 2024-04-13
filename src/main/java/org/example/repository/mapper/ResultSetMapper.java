package org.example.repository.mapper;

import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public interface ResultSetMapper {

    List<Book> map2BookList(ResultSet rs) throws SQLException;

    Book map2Book(ResultSet rs) throws SQLException;

    List<Author> map2AuthorList(ResultSet rs) throws SQLException;

    Author map2Author(ResultSet rs) throws SQLException;

    List<Category> map2CategoryList(ResultSet rs) throws SQLException;

    Category map2Category(ResultSet rs) throws SQLException;


}