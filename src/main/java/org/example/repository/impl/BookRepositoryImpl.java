package org.example.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.db.ConnectionManager;
import org.example.model.Book;
import org.example.model.Category;
import org.example.repository.BookRepository;
import org.example.repository.exception.DBException;
import org.example.repository.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    private final ConnectionManager connectionManager;
    private final ResultSetMapper resultSetMapper;

    public BookRepositoryImpl(ConnectionManager connectionManager, ResultSetMapper resultSetMapper) {
        this.connectionManager = connectionManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public List<Book> findAll() {
        String selectBooksSql = "SELECT *  FROM books " +
                "left join (select a.id as author_id, a.name as author_name from authors a) a " +
                "on a.author_id = books.author_id ";
        String selectCategoriesSql = "SELECT *  FROM categories c " +
                "join book2category b2c on c.id = b2c.category_id where b2c.book_id = ?";
        ResultSet categoryRs = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement booksPS = connection.prepareStatement(selectBooksSql);
             PreparedStatement categoriesPS = connection.prepareStatement(selectCategoriesSql);
             ResultSet booksRS = booksPS.executeQuery()) {

            List<Book> books = resultSetMapper.map2BookList(booksRS);
            for (Book book : books) {
                categoriesPS.setLong(1, book.getId());
                categoryRs = categoriesPS.executeQuery();
                List<Category> categories = resultSetMapper.map2CategoryList(categoryRs);
                book.setCategories(categories);
            }
            return books;

        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            try {
                if (categoryRs != null) {
                    categoryRs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String selectBooksSql = "SELECT *  FROM books where id = ?";
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectBooksSql)) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSetMapper.map2Book(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void deleteById(Long id) {
        String deleteBookSql = "DELETE FROM books WHERE id = ?";
        String deleteBook2CategorySql = "DELETE FROM book2category where book_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteBookPS = connection.prepareStatement(deleteBookSql);
             PreparedStatement deleteBook2CategoryPS = connection.prepareStatement(deleteBook2CategorySql)) {
            try {
                connection.setAutoCommit(false);
                deleteBook2CategoryPS.setLong(1, id);
                deleteBook2CategoryPS.executeUpdate();

                deleteBookPS.setLong(1, id);
                deleteBookPS.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new DBException(e);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void save(Book item) {
        String insertBookSql = "INSERT INTO books(title, author_id) values ( ?, ?)";
        String selectBookSql = "SELECT id from books where title = ? and (author_id = ? OR author_id ISNULL)";
        String insertBook2Category = "INSERT INTO book2category(book_id, category_id) VALUES (?,?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertBookPs = connection.prepareStatement(insertBookSql);
             PreparedStatement selectBookPS = connection.prepareStatement(selectBookSql);
             PreparedStatement insertBook2CategoryPS = connection.prepareStatement(insertBook2Category)) {

            try {
                connection.setAutoCommit(false);
                long bookId = saveBookAndGetBookId(item, insertBookPs, selectBookPS);
                saveBookCategories(item, insertBook2CategoryPS, bookId);
            } catch (Exception e) {
                connection.rollback();
                throw new DBException(e);
            } finally {
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static long saveBookAndGetBookId(Book item, PreparedStatement insertBookPs,
                                             PreparedStatement selectBookPS) throws SQLException {
        insertBookPs.setString(1, item.getTitle());
        if (item.getAuthor() != null) {
            insertBookPs.setLong(2, item.getAuthor().getId());
        } else {
            insertBookPs.setNull(2, Types.BIGINT);
        }
        insertBookPs.execute();
        selectBookPS.setString(1, item.getTitle());
        if (item.getAuthor() != null) {
            selectBookPS.setLong(2, item.getAuthor().getId());
        } else {
            selectBookPS.setNull(2, Types.BIGINT);
        }
        ResultSet resultSet = selectBookPS.executeQuery();
        long bookId;
        if (resultSet.next()) {
            bookId = resultSet.getLong("id");
        } else {
            throw new DBException("Book not found");
        }
        resultSet.close();
        return bookId;
    }

    private static void saveBookCategories(Book item,
                                           PreparedStatement insertBook2CategoryPS,
                                           long bookId) throws SQLException {
        if (CollectionUtils.isNotEmpty(item.getCategories())) {
            for (Category category : item.getCategories()) {
                Long id = category.getId();
                insertBook2CategoryPS.setLong(1, bookId);
                insertBook2CategoryPS.setLong(2, id);
                insertBook2CategoryPS.addBatch();
            }
            insertBook2CategoryPS.executeBatch();
        }
    }

    @Override
    public void update(Book item) {
        String deleteBook2CategorySql = "delete from book2category where book_id = ?";
        String insertBook2CategorySql = "insert into book2category(book_id, category_id) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection()) {

            PreparedStatement updateBookPS = null;
            if (StringUtils.isNotBlank(item.getTitle()) || item.getAuthor() != null) {
                updateBookPS = connection.prepareStatement(createUpdateBookSQL(item));
            }

            try (PreparedStatement deleteBook2CategoryPS = connection.prepareStatement(deleteBook2CategorySql);
                 PreparedStatement insertBook2CategoryPS = connection.prepareStatement(insertBook2CategorySql)) {
                connection.setAutoCommit(false);
                updateBook(item, updateBookPS);
                changeBookCategories(item, connection, deleteBook2CategoryPS, insertBook2CategoryPS);
            } catch (Exception e) {
                connection.rollback();
                throw new DBException(e);
            } finally {
                if (updateBookPS != null) {
                    updateBookPS.close();
                }
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    private static void updateBook(Book item, PreparedStatement updateBookPS) throws SQLException {
        if (updateBookPS != null) {
            if (StringUtils.isNotBlank(item.getTitle()) && item.getAuthor() != null) {
                updateBookPS.setString(1, item.getTitle());
                updateBookPS.setLong(2, item.getAuthor().getId());
                updateBookPS.setLong(3, item.getId());
            } else {
                if (StringUtils.isNotBlank(item.getTitle())) {
                    updateBookPS.setString(1, item.getTitle());
                }
                if (item.getAuthor() != null) {
                    updateBookPS.setLong(1, item.getAuthor().getId());
                }
                updateBookPS.setLong(2, item.getId());
            }
            updateBookPS.executeUpdate();
        }
    }

    private static void changeBookCategories(Book item, Connection connection,
                                             PreparedStatement deleteBook2CategoryPS,
                                             PreparedStatement insertBook2CategoryPS) throws SQLException {
        if (CollectionUtils.isNotEmpty(item.getCategories())) {
            deleteBook2CategoryPS.setLong(1, item.getId());
            deleteBook2CategoryPS.executeUpdate();
            for (Category category : item.getCategories()) {
                insertBook2CategoryPS.setLong(1, item.getId());
                insertBook2CategoryPS.setLong(2, category.getId());
                insertBook2CategoryPS.addBatch();
            }
            insertBook2CategoryPS.executeBatch();
        }
        connection.commit();
    }


    private static String createUpdateBookSQL(Book item) {
        String updateBookSql = null;
        if (StringUtils.isNotBlank(item.getTitle()) && item.getAuthor() != null) {
            updateBookSql = "UPDATE  books SET title = ?, author_id = ? WHERE id = ?";
        } else if (StringUtils.isNotBlank(item.getTitle()) ^ item.getAuthor() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE  books SET ");
            if (StringUtils.isNotBlank(item.getTitle())) {
                sb.append("title = ? ");
            }
            if (item.getAuthor() != null) {
                sb.append("author_id = ? ");
            }
            sb.append("WHERE id = ?");
            updateBookSql = sb.toString();
        }

        return updateBookSql;
    }

}
