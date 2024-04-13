package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.repository.exception.DBException;
import org.example.repository.mapper.ResultSetMapper;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final ConnectionManager connectionManager;
    private final ResultSetMapper resultSetMapper;

    public CategoryRepositoryImpl(ConnectionManager connectionManager,
                                  ResultSetMapper resultSetMapper) {
        this.connectionManager = connectionManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT *  FROM categories";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSetMapper.map2CategoryList(resultSet);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public Optional<Category> findById(Long id) {
        String sql = "SELECT *  FROM categories where id = ?";
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSetMapper.map2Category(resultSet));
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

    public List<Category> findById(List<Long> ids) {
        String sql = "SELECT *  FROM categories where id = ANY (?)";
        ResultSet resultSet = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            Array idsArray = ps.getConnection().createArrayOf("BIGINT", ids.toArray());
            ps.setArray(1, idsArray);
            resultSet = ps.executeQuery();
            return resultSetMapper.map2CategoryList(resultSet);
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
        String deleteCategorySql = "DELETE FROM categories WHERE id = ?";
        String deleteBook2CategorySql = "DELETE FROM book2category where category_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteCategoryPS = connection.prepareStatement(deleteCategorySql);
             PreparedStatement deleteBook2CategoryPS = connection.prepareStatement(deleteBook2CategorySql)) {
            deleteBook2CategoryPS.setLong(1, id);
            deleteBook2CategoryPS.executeUpdate();
            deleteCategoryPS.setLong(1, id);
            deleteCategoryPS.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    @Override
    public void save(Category item) {
        String sql = "INSERT INTO categories(category_name) VALUES (?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void update(Category item) {
        String sql = "update  categories set category_name = ? where id = ?";
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
