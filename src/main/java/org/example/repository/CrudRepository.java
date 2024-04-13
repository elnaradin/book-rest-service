package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, K> {

    List<T> findAll();

    Optional<T> findById(K id);

    void deleteById(K id);

    void save(T item);

    void update(T item);
}
