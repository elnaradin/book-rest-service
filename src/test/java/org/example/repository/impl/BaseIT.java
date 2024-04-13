package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.repository.mapper.ResultSetMapper;
import org.example.repository.mapper.ResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseIT {
    protected ConnectionManager cm;
    protected ResultSetMapper rsMapper;
    public static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:14")
                    .withInitScript("db-migration.sql")
                    .withPassword("test");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        cm = new ConnectionManagerImpl(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        rsMapper = new ResultSetMapperImpl();
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = cm.getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("truncate categories cascade");
            statement.executeUpdate("truncate authors cascade ");
            statement.executeUpdate("truncate books cascade ");
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
