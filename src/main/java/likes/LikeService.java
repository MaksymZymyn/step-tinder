package likes;

import lombok.*;
import utils.exceptions.InvalidLikeDataException;

import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
@Data
public class LikeService {
    LikeDAO likeDAO;

    @SneakyThrows(SQLException.class)
    public void insert(Like like) {
        likeDAO.insert(like);
    }

    @SneakyThrows({SQLException.class, InvalidLikeDataException.class})
    public Optional<Like> get(UUID id) {
        return likeDAO.get(id);
    }

    @SneakyThrows(SQLException.class)
    public boolean getByChoice(UUID user_to, boolean liked) {
        List<Like> likes = likeDAO.getByChoice(liked);
        return likes.stream().anyMatch(like -> like.getUser_to().equals(user_to));
    }
}
