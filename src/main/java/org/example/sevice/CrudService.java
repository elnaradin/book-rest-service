package org.example.sevice;

import java.util.List;

public interface CrudService<T> {
    void add(T item);

    List<T> getAll();

    void deleteById(long id);

    void update(T item);
}
