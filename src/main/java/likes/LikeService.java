package likes;

import lombok.*;
import java.util.*;

@AllArgsConstructor
@Data
public class LikeService {
    LikeDAO likeDAO;

    public void insert(Like like) {
        likeDAO.insert(like);
    }

    public Optional<Like> get(UUID id) {
        return likeDAO.get(id);
    }

    public boolean getByChoice(UUID user_to, boolean liked) {
        List<Like> likes = likeDAO.getByChoice(liked);
        return likes.stream().anyMatch(like -> like.getUser_to().equals(user_to));
    }
}
