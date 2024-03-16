package users;

import utils.exceptions.InvalidUserDataException;

import java.sql.SQLException;
import java.util.UUID;

public class UserService {
    private final UserDAO dao;

    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public User get(String username) throws SQLException, InvalidUserDataException {
        return dao.get(username);
    }

    public User get(UUID id) throws SQLException, InvalidUserDataException {
        return dao.get(id);
    }

    public User insert(String username, String fullName, String picture, String password) throws SQLException {
        return dao.insert(username, fullName, picture, password);
    }
}
