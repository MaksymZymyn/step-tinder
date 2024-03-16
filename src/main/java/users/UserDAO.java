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

    public void insert(String username, String fullName, String picture, String password) throws SQLException {
        try (Connection conn = Database.connect()) {
            String insert = """
                    INSERT INTO users (username, full_name, picture, password)
                    values (?, ?, ?, ?)
                    """;

            PreparedStatement st = conn.prepareStatement(insert);
            String pass = Password.hash(password);

            st.setString(1, username);
            st.setString(2, fullName);
            st.setString(3, picture);
            st.setString(4, pass);

            int ignoredN = st.executeUpdate();
        }
    }
}
