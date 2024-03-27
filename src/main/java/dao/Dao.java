package dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, E> {
    List<T> findAll();

    Optional<T> findByCode(E entity);

    void add(E entity);
}