package likes;

import lombok.*;
import users.User;
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

    public List<User> getLikedUsers(UUID userId) {
        return likeDAO.getLikedUsers(userId);
    }
}
