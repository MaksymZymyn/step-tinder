package users;

import database.Database;
import exceptions.DatabaseException;
import exceptions.NotFoundException;
import utils.environment.ConnectionDetails;
import utils.environment.HerokuEnv;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class UsersService {
    Connection conn = Database.connect(HerokuEnv.jdbc_url(), HerokuEnv.jdbc_username(), HerokuEnv.jdbc_password());

    private final UsersDAO db = new UsersDAO(conn);

    public List<User> getLikedUsers(Integer currentId) throws DatabaseException, NotFoundException {
        List<User> likedUsers = db.getLikedUsers(currentId);
        if (likedUsers.isEmpty()) {
            throw new NotFoundException(String.format("Users with like for user ID: %d not found", currentId));
        }
        return likedUsers;
    }

    public User getByEmail(String email) throws NotFoundException, DatabaseException {
        Optional<User> byEmail = db.getByEmail(email);
        if (byEmail.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return byEmail.get();
    }

    public User getById(Integer id) throws NotFoundException, DatabaseException {
        Optional<User> user = db.get(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return user.get();
    }

    public User login(String email, String password) throws NotFoundException, DatabaseException, IllegalArgumentException {
        User user = getByEmail(email);
        boolean equalsPassword = user.getPassword().equals(password);
        if (!equalsPassword) {
            throw new IllegalArgumentException("Invalid password");
        }
        return user;
    }

    public List<User> getAll() throws DatabaseException {
        return db.getAll();
    }

    public Optional<User> get(int id) throws DatabaseException {
        return db.get(id);
    }

    public List<User> getBy(Predicate<User> p) throws DatabaseException {
        return db.getBy(p);
    }

    public void put(User user) throws DatabaseException {
        db.put(user);
    }

    public void delete(int id) throws DatabaseException {
        db.delete(id);
    }

    public User create(ResultSet resultSet) throws SQLException {
        return db.create(resultSet);
    }
}
