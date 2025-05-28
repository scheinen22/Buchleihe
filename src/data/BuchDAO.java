package data;

import java.util.List;

import model.Buch;

// Interface für das Data Access Objekt, Implementierung der CRUD Operationen
public interface BuchDAO {
    void save(Buch buch);
    Buch findById(int id);
    void update(Buch buch);
    void delete(Buch buch);
    List<Buch> findAll();
}
