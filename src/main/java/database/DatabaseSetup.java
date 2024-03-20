package database;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

public class DatabaseSetup {
  public static void migrate(String url, String username, String password) {
    FluentConfiguration config = new FluentConfiguration().dataSource(
        url,
        username,
        password
    );
    Flyway flyway = new Flyway(config);
    flyway.migrate();
  }
}
