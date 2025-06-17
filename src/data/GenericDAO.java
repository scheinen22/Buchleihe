package data;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface für die Data Access Objekte. Die Standard-CRUD-Operationen werden vorrausgesetzt
 * und sind Pflicht in der Implementierung. Durch den Typparameter als Generic kann
 * dieses Interface als Vorlage genutzt werden. Jede Tabelle braucht ein DAO.
 * @param <T>
 */
// Interface für das Data Access Objekt, Implementierung der CRUD Operationen
public interface GenericDAO<T> {
    void save(@NotNull T entity);
    @Nullable T findById(int id);
    void update(@NotNull T entity);
    void delete(@NotNull T entity);
    List<T> getAll();
}
