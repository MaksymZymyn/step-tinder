package likes;

import database.Database;
import lombok.SneakyThrows;
import users.User;
import utils.exceptions.InvalidLikeDataException;
import utils.exceptions.InvalidUserDataException;
import utils.interfaces.DAO;
import java.sql.*;
import java.util.*;

public class LikeDAO implements DAO<Like> {

    @Override
    @SneakyThrows(SQLException.class)
    public void insert(Like like) {

        try (Connection connection = Database.connect()) {
            String sql = "INSERT INTO likes(user_from, user_to, value) " +
                         "VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, like.getUser_from());
            statement.setObject(2, like.getUser_to());
            statement.setBoolean(3, like.isValue());

            statement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows({SQLException.class, InvalidLikeDataException.class})
    public Optional<Like> get(UUID id) {

        try (Connection connection = Database.connect()) {
            String sql = "SELECT * FROM likes WHERE user_from = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(Like.fromRS(resultSet));
            }

        }
        return Optional.empty();
    }

    @SneakyThrows(SQLException.class)
    public List<User> getLikedUsers(UUID userId) {
        List<User> likedUsers = new ArrayList<>();
        try (Connection conn = Database.connect()) {
            String selectLikedUsers = """
                SELECT u.id, u.username, u.full_name, u.picture 
                FROM users u
                JOIN likes l ON u.id = l.user_to
                WHERE l.value = true AND l.user_from=?
                """;

            try (PreparedStatement st = conn.prepareStatement(selectLikedUsers)) {
                st.setObject(1, userId);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    likedUsers.add(User.fromRS(rs));
                }
            }
        } catch (InvalidUserDataException e) {
            throw new RuntimeException("Invalid user data retrieved from the database", e);
        }
        return likedUsers;
    }
}
