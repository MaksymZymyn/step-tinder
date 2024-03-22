package database;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import utils.environment.HerokuEnv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());
    }

    public static void setup() {
        FluentConfiguration config = new FluentConfiguration().dataSource(
                HerokuEnv.jdbc_url(),
                HerokuEnv.jdbc_username(),
                HerokuEnv.jdbc_password()
        );
        Flyway flyway = new Flyway(config);
        flyway.migrate();
    }

}
