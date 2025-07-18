package server.service;

import client.exception.CheckedException;
import client.interfaces.NutzerService;
import client.model.Nutzer;
import server.data.NutzerDAO;
import client.view.View;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Implementiert das NutzerService-Interface. Diese Klasse enthält die gesamte Geschäftslogik
 * für Prozesse rund um das Verwalten von Nutzern und den Login. Sie wird auf dem Server ausgeführt,
 * koordiniert die Interaktionen zwischen den zugehörigen DAOs und wird von Clients remote aufgerufen.
 */
public class NutzerServiceImpl extends UnicastRemoteObject implements NutzerService {

    private final NutzerDAO nutzerDAO;

    /**
     * Erstellt eine neue Instanz des NutzerService.
     *
     * @param nutzerDAO Das DAO für den Zugriff auf Nutzer-Daten.
     */
    public NutzerServiceImpl(NutzerDAO nutzerDAO) throws RemoteException {
        this.nutzerDAO = nutzerDAO;
    }

    /**
     * Authentifiziert einen Nutzer anhand von Benutzername und Passwort.
     *
     * @param benutzername Der eingegebene Benutzername.
     * @param passwort     Das eingegebene Passwort.
     * @return Das {@link Nutzer}-Objekt bei erfolgreicher Authentifizierung, ansonsten {@code null}.
     */
    public Nutzer authentifizieren(String benutzername, String passwort) {
        return nutzerDAO.findByBenutzernameUndPasswort(benutzername, passwort);
    }

    /**
     * Ruft eine Liste aller registrierten Nutzer ab.
     *
     * @return Eine Liste mit allen Nutzern.
     */
    public List<Nutzer> alleNutzer() {
        return nutzerDAO.getAll();
    }

    /**
     * Löscht einen Nutzer anhand seiner ID aus dem System.
     *
     * @param id Die ID des zu löschenden Nutzers.
     * @throws CheckedException Wenn kein Nutzer mit der angegebenen ID existiert.
     */
    public void nutzerLoeschen(int id) throws CheckedException {
        Nutzer nutzer = nutzerDAO.findById(id);
        if (nutzer != null && nutzer.getCustomerId() == id) {
            // Hier muss man in der DB ein ON DELETE CASCADE oder ON DELETE NULL setzen
            nutzerDAO.delete(nutzer);
            View.ausgabe("Nutzer wurde gelöscht!");
        } else {
            throw new CheckedException("Nutzer existiert nicht.");
        }
    }
}
