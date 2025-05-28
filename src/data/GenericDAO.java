package data;

import java.util.List;

import model.Buch;

// Interface für das Data Access Objekt, Implementierung der CRUD Operationen
public interface GenericDAO<T> {
    void save(T object);
    Buch findById(int id);
    void update(T object);
    void delete(T object);
    List<T> findAll();
}
