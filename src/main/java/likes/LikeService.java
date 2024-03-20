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

    public List<Like> getByChoice(boolean liked) {
        return likeDAO.getByChoice(liked);
    }

    public boolean hasBeenLiked(UUID user_to) {
        List<Like> likes = likeDAO.getByChoice(true);
        return likes.stream().anyMatch(like -> like.getUser_to().equals(user_to));
    }
}
