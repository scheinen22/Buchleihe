package client.interfaces;

import client.exception.CheckedException;
import client.model.Buch;
import client.model.Nutzer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote-Interface für den AusleiheService.
 * Definiert alle Methoden, die vom Client remote aufgerufen werden können.
 */
public interface AusleiheService extends Remote {

    /**
     * Sucht ein Buch anhand seiner ID (ISBN).
     * @param id Die ID des Buches.
     * @return Das gefundene Buch-Objekt.
     * @throws RemoteException bei Netzwerkfehlern.
     * @throws CheckedException wenn kein Buch gefunden wird.
     */
    Buch sucheBuch(int id) throws RemoteException, CheckedException;

    /**
     * Leiht ein Buch für einen Nutzer aus.
     * @param buchId Die ID des Buches.
     * @param nutzer Der Nutzer, der ausleiht.
     * @throws RemoteException bei Netzwerkfehlern.
     * @throws CheckedException bei Logikfehlern (z.B. Buch schon verliehen).
     */
    void ausleihen(int buchId, Nutzer nutzer) throws RemoteException, CheckedException;

    /**
     * Gibt ein Buch zurück.
     * @param buchId Die ID des Buches.
     * @param nutzer Der Nutzer, der zurückgibt.
     * @throws RemoteException bei Netzwerkfehlern.
     * @throws CheckedException wenn der Nutzer das Buch nicht ausgeliehen hat.
     */
    void rueckgabe(int buchId, Nutzer nutzer) throws RemoteException, CheckedException;

    /**
     * Holt eine Liste aller Bücher im Katalog.
     * @return Eine Liste aller Bücher.
     * @throws RemoteException bei Netzwerkfehlern.
     */
    List<Buch> holeAlleBuecher() throws RemoteException;
}