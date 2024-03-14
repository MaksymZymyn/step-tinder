package likes;

import database.Database;
import utils.dao.DAO;
import utils.environment.ConnectionDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LikesDAO implements DAO<Like>{

    private Connection getConnection() throws SQLException {
        return Database.create(ConnectionDetails.getUrl(), ConnectionDetails.getUsername(), ConnectionDetails.getPassword());
    }

    private Like createLikeFromResultSet(ResultSet resultSet) throws SQLException {
        return new Like(
                resultSet.getInt("id"),
                resultSet.getInt("user_from"),
                resultSet.getInt("user_to"),
                resultSet.getBoolean("value"),
                resultSet.getTimestamp("created_at").getTime());
    }

    @Override
    public List<Like> getAll() {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT * FROM user_likes";
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Like like = createLikeFromResultSet(resultSet);
                likes.add(like);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return likes;
    }

    @Override
    public Optional<Like> get(int likeId) {
        String sql = "SELECT * FROM user_likes WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, likeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Like like = createLikeFromResultSet(resultSet);
                return Optional.of(like);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Like> getBy(Predicate<Like> p) {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT * FROM user_likes";
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Like like = createLikeFromResultSet(resultSet);

                if (p.test(like)) {
                    likes.add(like);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return likes;
    }

    @Override
    public void put(Like like) {
        String sql = "INSERT INTO user_likes (user_from, user_to, value) VALUES (?, ?, ?)";
        try (Connection conn = Database.create(ConnectionDetails.getUrl(), ConnectionDetails.getUsername(), ConnectionDetails.getPassword());
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, like.getUser_from());
            preparedStatement.setInt(2, like.getUser_to());
            preparedStatement.setBoolean(3, like.getValue());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        List<Like> likes = new ArrayList<>();
        String sql = "DELETE FROM user_likes WHERE id = ?";
        try (Connection conn = Database.create(ConnectionDetails.getUrl(), ConnectionDetails.getUsername(), ConnectionDetails.getPassword());
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Like> findByUserFrom(Integer user_fromId) throws SQLException{
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT * FROM user_likes WHERE user_from = ?";
        try (Connection conn = Database.create(ConnectionDetails.getUrl(), ConnectionDetails.getUsername(), ConnectionDetails.getPassword());
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

             preparedStatement.setInt(1, user_fromId);
             ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Like like = createLikeFromResultSet(resultSet);
                likes.add(like);
            }
        }
        return likes;
    }
}
