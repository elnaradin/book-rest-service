package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.Author;
import org.example.repository.AuthorRepository;
import org.example.repository.exception.DBException;
import org.example.repository.mapper.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AuthorRepositoryImpl implements AuthorRepository {
    private final ConnectionManager connectionManager;
    private final ResultSetMapper resultSetMapper;

    public AuthorRepositoryImpl(ConnectionManager connectionManager,
                                ResultSetMapper resultSetMapper) {
        this.connectionManager = connectionManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT *  FROM authors";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSetMapper.map2AuthorList(resultSet);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = "SELECT *  FROM authors where id = ?";
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSetMapper.map2Author(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "DELETE FROM authors WHERE id = ?";
        String updateBooksSql = "UPDATE books SET author_id = NULL WHERE author_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteAuthorPS = connection.prepareStatement(deleteSql);
             PreparedStatement updateBooksPS = connection.prepareStatement(updateBooksSql)) {
            try {
                connection.setAutoCommit(false);
                updateBooksPS.setLong(1, id);
                updateBooksPS.executeUpdate();
                deleteAuthorPS.setLong(1, id);
                deleteAuthorPS.executeUpdate();
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
    public void save(Author item) {
        String sql = "INSERT INTO authors(name) VALUES (?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(item.getName()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void update(Author item) {
        String sql = "update  authors set name = ? where id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setLong(2, item.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}
