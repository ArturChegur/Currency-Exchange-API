package service;

import java.sql.SQLException;
import java.util.List;

public interface Service<T> {
    List<T> findAll() throws SQLException;
    T findByCode(String code) throws SQLException;
    void add(String value1, String value2, String value3) throws SQLException;
    boolean exists(String code) throws SQLException;
}