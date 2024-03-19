package likes;

import utils.exceptions.InvalidLikeDataException;
import java.sql.*;
import java.util.UUID;

public class Like {

    private final UUID id;
    private final UUID user_from;
    private final UUID user_to;
    private final boolean value;

    public Like(UUID id, UUID user_from, UUID user_to, boolean value) {
        this.id = id;
        this.user_from = user_from;
        this.user_to = user_to;
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUser_from() {
        return user_from;
    }

    public UUID getUser_to() {
        return user_to;
    }

    public boolean isValue() {
        return value;
    }

    public static Like fromRS(ResultSet rs) throws SQLException, InvalidLikeDataException {
        UUID id = rs.getObject("id", UUID.class);
        UUID user_from = rs.getObject("user_from", UUID.class);
        UUID user_to = rs.getObject("user_to", UUID.class);
        Boolean value = rs.getBoolean("value");

        if (id == null || user_from == null || user_to == null || value == null) throw new InvalidLikeDataException();

        return new Like(id, user_from, user_to, value);
    }
}