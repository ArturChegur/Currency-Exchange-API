package dao;

public interface Dao<K, T> {
    List<T> findAll();
    Optional<T> findById(K id);
    boolean delete

}
