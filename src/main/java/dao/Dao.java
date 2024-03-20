package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll() throws SQLException;
    Optional<T> findByCode(String code) throws SQLException;
    void add(T entity) throws SQLException;
}