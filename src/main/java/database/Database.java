package database;

import lombok.SneakyThrows;
import utils.environment.HerokuEnv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    @SneakyThrows(SQLException.class)
    public static Connection connect() {
        return DriverManager.getConnection(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());
    }
}
