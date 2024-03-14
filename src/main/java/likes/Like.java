package likes;

import utils.dao.HasId;

import java.io.Serial;
import java.io.Serializable;

public class Like implements Serializable, HasId {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int id;
    private final int user_from;
    private final int user_to;
    private final boolean value;
    private final long created_at;

    public Like(int id, int user_from, int user_to, boolean value, long created_at) {
        this.id = id;
        this.user_from = user_from;
        this.user_to = user_to;
        this.value = value;
        this.created_at = created_at;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getUser_from() {
        return user_from;
    }

    public int getUser_to() {
        return user_to;
    }

    public boolean getValue() {
        return value;
    }

    public long getCreated_at() {
        return created_at;
    }
}
