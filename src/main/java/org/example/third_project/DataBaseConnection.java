package org.example.third_project;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public final class DataBaseConnection {
    private static final HikariDataSource hikariDataSource;
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite::resource:database.sqlite";

    static {
        HikariConfig config = hikariConfig();
        hikariDataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public static Connection get() {
        return hikariDataSource.getConnection();
    }

    private static HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(DRIVER);
        config.setJdbcUrl(URL);
        return config;
    }
}
