package users;

import utils.exceptions.RegistrationException;
import utils.exceptions.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserDAO dao;

    public UserService(UserDAO dao) {
        this.dao = dao;
    }

    public User get(String username) throws SQLException, UserNotFoundException {
        return dao.get(username).orElseThrow(UserNotFoundException::new);
    }

    public User get(UUID id) throws SQLException, UserNotFoundException {
        return dao.get(id).orElseThrow(UserNotFoundException::new);
    }

    public List<User> getAllExcept(UUID userId) throws SQLException {
        return dao.getAllExcept(userId);
    }

    public User insert(String username, String fullName, String picture, String password) throws SQLException, RegistrationException {
        dao.insert(User.make(username, fullName, picture, password));
        return dao.get(username).orElseThrow(RegistrationException::new);
    }
}
