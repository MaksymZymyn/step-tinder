package users;

import database.Database;
import utils.exceptions.InvalidUserDataException;
import utils.misc.Password;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {

  public User get(String username) throws SQLException, InvalidUserDataException {
    try (Connection conn = Database.connect()) {
      String select = """
                      SELECT id, username, full_name, picture, password
                      FROM users
                      WHERE username=?
                      """;

      PreparedStatement st = conn.prepareStatement(select);
      st.setString(1, username);

      ResultSet rs = st.executeQuery();
      rs.next();

      return User.fromRS(rs);
    }
  }

  public User get(UUID id) throws SQLException, InvalidUserDataException {
    try (Connection conn = Database.connect()) {
      String select = """
                      SELECT id, username, full_name, picture, password
                      FROM users
                      WHERE id=?
                      """;

      PreparedStatement st = conn.prepareStatement(select);
      st.setObject(1, id);

      ResultSet rs = st.executeQuery();
      rs.next();

      return User.fromRS(rs);
    }
  }

  public User insert(String username, String fullName, String picture, String password) throws SQLException {
    try (Connection conn = Database.connect()) {
      String insert = """
                      INSERT INTO users (id, username, full_name, picture, password)
                      VALUES (?, ?, ?, ?, ?)
                      """;

      PreparedStatement st = conn.prepareStatement(insert);
      UUID uuid = UUID.randomUUID();
      String pass = Password.hash(password);

      st.setObject(1, uuid);
      st.setString(2, username);
      st.setString(3, fullName);
      st.setString(4, picture);
      st.setString(5, pass);

      int ignoredN = st.executeUpdate();

      return new User(uuid, username, fullName, picture, pass);
    }
  }
}
