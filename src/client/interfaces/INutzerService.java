package client.interfaces;

import client.exception.CheckedException;
import client.model.Nutzer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote-Interface für den NutzerService.
 */
public interface INutzerService extends Remote {

    /**
     * Authentifiziert einen Nutzer anhand von Benutzername und Passwort.
     * @param benutzername Der Benutzername.
     * @param passwort Das Passwort.
     * @return Das Nutzer-Objekt bei Erfolg, sonst null.
     * @throws RemoteException bei Netzwerkfehlern.
     */
    Nutzer authentifizieren(String benutzername, String passwort) throws RemoteException;

    /**
     * Holt eine Liste aller Nutzer.
     * @return Eine Liste aller Nutzer.
     * @throws RemoteException bei Netzwerkfehlern.
     */
    List<Nutzer> alleNutzer() throws RemoteException;

    /**
     * Löscht einen Nutzer anhand seiner ID.
     * @param id Die ID des zu löschenden Nutzers.
     * @throws RemoteException bei Netzwerkfehlern.
     * @throws CheckedException wenn der Nutzer nicht existiert.
     */
    void nutzerLoeschen(int id) throws RemoteException, CheckedException;

    /**
     * Gibt die Profildaten eines Nutzers auf der Konsole aus.
     *
     * @param nutzer Der Nutzer, dessen Profil angezeigt werden soll.
     */
    void profilAnzeigen(Nutzer nutzer) throws RemoteException;
}
