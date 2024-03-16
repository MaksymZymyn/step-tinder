package likes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class LikeService {
    private final LikeDAO likeDAO;

    public LikeService(LikeDAO likeDAO) {
        this.likeDAO = likeDAO;
    }

    public void save(Like like) {
        likeDAO.save(like);
    }

    public List<Like> getAll() {
        return likeDAO.getAll();
    }

    public Optional<Like> get(UUID id) {
        return likeDAO.get(id);
    }

    public boolean hasBeenLiked(UUID id, String user_to) {
        List<Like> likes = likeDAO.getBy(like -> like.getId().equals(id));
        return likes.stream().anyMatch(like -> like.getUser_to().equals(user_to));
    }

    public void delete(UUID id) {
        likeDAO.delete(id);
    }
}
