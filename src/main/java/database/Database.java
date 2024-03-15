package database;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    @SneakyThrows
    public static Connection connect(String url, String username, String password) {
        return DriverManager.getConnection(url, username, password);
    }

    @SneakyThrows
    public static Connection createFromURL(String jdbc_url) {
        return DriverManager.getConnection(jdbc_url);
    }
}
