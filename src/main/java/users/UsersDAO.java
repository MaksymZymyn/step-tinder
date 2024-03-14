package users;

import logger.LoggerService;
import lombok.SneakyThrows;
import utils.dao.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UsersDAO implements DAO<User> {

  private final Connection conn;

  public UsersDAO(Connection conn) {
    this.conn = conn;
  }

  private final String SQL_getAll = "SELECT id as user_id, email, password FROM users";

  private final String SQL_get =
               """
               SELECT id as user_id,
                      email,
                      password        
               FROM users                              
               WHERE id = ?
               """;

  private final String SQL_put =
               """
               INSERT INTO users 
               (username, password, email, avatar, created_at) 
               VALUES(?, ?, ?, ?, ?);
               """;

  private final String SQL_delete = "DELETE FROM users WHERE id = ?";

  private final String SQL_selectLikedUsers =
                """
                SELECT u.id as user_id,
                       u.username,
                       u.email,                       
                       u.avatar,
                       l.time as lastLogin
                FROM user_likes ul
                LEFT JOIN users u on u.id = ul.user_to                
                WHERE ul.user_from = u.id and ul.value = TRUE
                ORDER BY u.id;
                """;

  private final String SQL_get_byEmail =
                """
                SELECT id as user_id,
                       email,
                       password        
                FROM users                              
                WHERE email = ?
                """;

  @SneakyThrows
  @Override
  public List<User> getAll() {
    try (PreparedStatement stmt = conn.prepareStatement(SQL_getAll)) {
      ResultSet rset = stmt.executeQuery();
      List<User> data = extractUsersFromResultSet(rset);
      LoggerService.info("Retrieved all users successfully");
      return data;
    } catch (Exception e) {
      LoggerService.error("Error occurred while retrieving all users: " + e.getMessage());
      throw e;
    }
  }

  @SneakyThrows
  @Override
  public Optional<User> get(int id) {
    try (PreparedStatement stmt = conn.prepareStatement(SQL_get)) {
      stmt.setInt(1, id);
      try (ResultSet rset = stmt.executeQuery()) {
        if (!rset.next()) {
          LoggerService.info("User with ID " + id + " not found");
          return Optional.empty();
        }
        User user = create(rset);
        LoggerService.info("Retrieved user with ID " + id + " successfully");
        return Optional.of(user);
      }
    } catch (Exception e) {
      LoggerService.error("Error occurred while retrieving user with ID " + id + ": " + e.getMessage());
      throw e;
    }
  }

  private List<User> extractUsersFromResultSet(ResultSet resultSet) throws SQLException {
    List<User> users = new ArrayList<>();
    while (resultSet.next()) {
      users.add(create(resultSet));
    }
    return users;
  }

  public User create(ResultSet resultSet) throws SQLException {
    return new User(
            resultSet.getInt("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("email"),
            resultSet.getString("avatar"),
            false, // Undefined value - not present in the SQL
            resultSet.getLong("created_at")
    );
  }

  @SneakyThrows
  @Override
  public List<User> getBy(Predicate<User> p) {
    try {
      List<User> allUsers = getAll();
      List<User> filteredUsers = allUsers.stream()
              .filter(p)
              .collect(Collectors.toList());
      LoggerService.info("Retrieved users matching predicate successfully");
      return filteredUsers;
    } catch (Exception e) {
      LoggerService.error("Error occurred while retrieving users by predicate: " + e.getMessage());
      throw e;
    }
  }

  @SneakyThrows
  @Override
  public void put(User user) {
    try (PreparedStatement stmt = conn.prepareStatement(SQL_put)) {
      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getAvatar());
      stmt.execute();
      LoggerService.info("User added successfully: " + user);
    } catch (Exception e) {
      LoggerService.error("Error occurred while adding user: " + user + ", Error: " + e.getMessage());
      throw e;
    }
  }

  @SneakyThrows
  @Override
  public void delete(int id) {
    try (PreparedStatement stmt = conn.prepareStatement(SQL_delete)) {
      stmt.setInt(1, id);
      stmt.execute();
      LoggerService.info("User with ID " + id + " deleted successfully");
    } catch (Exception e) {
      LoggerService.error("Error occurred while deleting user with ID " + id + ": " + e.getMessage());
      throw e;
    }
  }

  @SneakyThrows
  public List<User> getLikedUsers(int userId) {
    try {
      PreparedStatement stmt = conn.prepareStatement(SQL_selectLikedUsers);
      stmt.setInt(1, userId);
      ResultSet rset = stmt.executeQuery();
      List<User> likedUsers = extractUsersFromResultSet(rset);
      LoggerService.info("Retrieved liked users for user with ID " + userId + " successfully");
      return likedUsers;
    } catch (Exception e) {
      LoggerService.error("Error occurred while retrieving liked users for user with ID " + userId + ": " + e.getMessage());
      throw e;
    }
  }

  @SneakyThrows
  public Optional<User> getByEmail(String email) {
    try {
      PreparedStatement stmt = conn.prepareStatement(SQL_get_byEmail);
      stmt.setString(1, email);
      ResultSet rset = stmt.executeQuery();

      if (!rset.next()) {
        LoggerService.info("User with email " + email + " not found");
        return Optional.empty();
      }

      User user = create(rset);
      LoggerService.info("Retrieved user with email " + email + " successfully");
      return Optional.of(user);
    } catch (Exception e) {
      LoggerService.error("Error occurred while retrieving user with email " + email + ": " + e.getMessage());
      throw e;
    }
  }
}
