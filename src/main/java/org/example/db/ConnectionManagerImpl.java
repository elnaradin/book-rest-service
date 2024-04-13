package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager {

    private final HikariDataSource ds;
    private final HikariConfig config;


    public ConnectionManagerImpl() {
        config = new HikariConfig("/datasource.properties");
        ds = new HikariDataSource(config);
    }

    public ConnectionManagerImpl(String jdbcUrl, String username, String password) {
        config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        ds = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();

    }
}