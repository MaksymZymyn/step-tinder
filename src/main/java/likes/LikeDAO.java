package likes;

import database.Database;
import lombok.SneakyThrows;
import utils.exceptions.InvalidLikeDataException;
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
    public List<Like> getByChoice(boolean value) {
        List<Like> filteredLikes = new ArrayList<>();

        try (Connection connection = Database.connect()) {
            String sql = "SELECT * FROM likes WHERE value=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, value);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                UUID user_from = UUID.fromString(resultSet.getString("user_from"));
                UUID user_to = UUID.fromString(resultSet.getString("user_to"));
                boolean likeValue = resultSet.getBoolean("value");
                Like like = new Like(id, user_from, user_to, likeValue);
                filteredLikes.add(like);
            }
        }

        return filteredLikes;
    }
}
