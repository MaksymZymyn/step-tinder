package users;

import lombok.Getter;
import utils.exceptions.InvalidUserDataException;
import utils.misc.Password;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String username;
    private final String fullName;
    private final String picture;
    private final String password;

    private User(UUID id, String username, String fullName, String picture, String password) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.picture = picture;
        this.password = password;
    }

    private User(String username, String fullName, String picture, String password) {
        this.id = null;
        this.username = username;
        this.fullName = fullName;
        this.picture = picture;
        this.password = password;
    }

    public static User make(String username, String fullName, String picture, String password) {
        return new User(username, fullName, picture, Password.hash(password));
    }

    public static User fromRS(ResultSet rs) throws SQLException, InvalidUserDataException {
        UUID id = rs.getObject("id", UUID.class);
        String username = rs.getString("username");
        String fullName = rs.getString("full_name");
        String picture = rs.getString("picture");
        String password = rs.getString("password");

        if (id == null || username == null || fullName == null || picture == null || password == null) throw new InvalidUserDataException();

        return new User(id, username, fullName, picture, password);
    }

    public boolean checkPassword(String pw) {
        return Password.check(pw, password);
    }

}
