package utils.interfaces;

import likes.Like;
import lombok.SneakyThrows;
import utils.exceptions.InvalidLikeDataException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface DAO<A> {
    List<A> getAll();
    List<A> getBy(Predicate<A> p);
    Optional<A> get(UUID id);
    void save(A a);

    void delete(UUID id);
}
