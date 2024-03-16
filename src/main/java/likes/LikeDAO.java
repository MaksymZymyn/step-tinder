package likes;

import database.Database;
import lombok.SneakyThrows;
import utils.exceptions.InvalidLikeDataException;
import utils.interfaces.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.function.Predicate;

public class LikeDAO implements DAO<Like> {

    @Override
    @SneakyThrows(SQLException.class)
    public void save(Like like) {

        try (Connection connection = Database.connect()) {
             String sql = "INSERT INTO likes(id, user_from, user_to, value, created_at) " +
                          "VALUES (?, ?, ?, ?, ?)";
             PreparedStatement statement = connection.prepareStatement(sql);
             UUID uuid = UUID.randomUUID();

             statement.setObject(1, uuid);
             statement.setObject(2, like.getUser_from());
             statement.setObject(3, like.getUser_to());
             statement.setBoolean(4, like.isValue());
             statement.setString(5, like.getCreated_at());

             statement.executeUpdate();
        }
    }

    @Override
    @SneakyThrows(SQLException.class)
    public List<Like> getAll() {
        List<Like> likes = new ArrayList<>();

        try (Connection connection = Database.connect()) {
            String sql = "SELECT * FROM likes";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                UUID user_from = UUID.fromString(resultSet.getString("user_from"));
                UUID user_to = UUID.fromString(resultSet.getString("user_to"));
                boolean value = resultSet.getBoolean("value");

                likes.add(new Like(id, user_from, user_to, value));
            }
        }
        return likes;
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

    @Override
    public List<Like> getBy(Predicate<Like> predicate) {
        List<Like> filteredLikes = new ArrayList<>();
        List<Like> allLikes = getAll();

        for (Like like : allLikes) {
            if (predicate.test(like)) {
                filteredLikes.add(like);
            }
        }

        return filteredLikes;
    }

    @Override
    @SneakyThrows(SQLException.class)
    public void delete(UUID id) {

        try (Connection connection = Database.connect()) {
             String sql = "DELETE FROM likes WHERE userId = ?";
             PreparedStatement statement = connection.prepareStatement(sql);
             statement.setObject(1, id);
             statement.execute();
        }
    }
}

