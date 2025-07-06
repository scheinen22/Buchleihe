package client.interfaces;

import client.exception.CheckedException;
import client.model.Nutzer;
import client.model.Vorschlag;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote-Interface für den VorschlagService.
 */
public interface IVorschlagService extends Remote {

    /**
     * Speichert einen neuen Buchvorschlag.
     * @param titel Der Buchtitel.
     * @param autor Der Autor.
     * @param nutzer Der vorschlagende Nutzer.
     * @throws RemoteException bei Netzwerkfehlern.
     */
    void buchVorschlagen(String titel, String autor, Nutzer nutzer) throws RemoteException;

    /**
     * Ruft alle offenen Vorschläge ab.
     * @return Eine Liste aller Vorschläge.
     * @throws RemoteException bei Netzwerkfehlern.
     */
    List<Vorschlag> alleVorschlaege() throws RemoteException;

    /**
     * Findet alle Vorschläge, für die ein Nutzer noch nicht benachrichtigt wurde.
     * @param nutzer Der betroffene Nutzer.
     * @return Eine Liste von Vorschlägen.
     * @throws RemoteException bei Netzwerkfehlern.
     */
    List<Vorschlag> nichtBenachrichtigteNutzer(Nutzer nutzer) throws RemoteException;

    /**
     * Akzeptiert einen Vorschlag und markiert ihn als bestellt.
     * @param id Die ID des Vorschlags.
     * @throws RemoteException bei Netzwerkfehlern.
     * @throws CheckedException wenn der Vorschlag nicht gefunden wird.
     */
    void alsBestelltMarkieren(int id) throws RemoteException, CheckedException;

    /**
     * Markiert einen Vorschlag als "benachrichtigt".
     * @param vorschlag Der zu aktualisierende Vorschlag.
     * @throws RemoteException bei Netzwerkfehlern.
     */
    void benachrichtigen(Vorschlag vorschlag) throws RemoteException;

    /**
     * Lehnt einen Vorschlag ab.
     * @param id Die ID des Vorschlags.
     * @throws RemoteException bei Netzwerkfehlern.
     * @throws CheckedException wenn der Vorschlag nicht gefunden wird.
     */
    void vorschlagAblehnen(int id) throws RemoteException, CheckedException;
}