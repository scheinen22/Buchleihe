package service;

import data.BuchDAO;
import data.VorschlagDAO;
import exception.CheckedException;
import model.Buch;
import model.Nutzer;
import model.Vorschlag;
import model.VorschlagsStatus;

import java.util.List;

/**
 * Vorschlagservice enthält die Businesslogik für die Vorschläge.
 * In dieser Klasse wird mit den besorgten Objekten aus der Datenbank hantiert.
 */
public class VorschlagService {

    private final VorschlagDAO vorschlagDAO;
    private final BuchDAO buchDAO;

    /**
     * Erstellt eine neue Instanz des VorschlagService.
     *
     * @param vorschlagDAO Das DAO für den Zugriff auf Vorschlags-Daten.
     * @param buchDAO      Das DAO für den Zugriff auf Buch-Daten, benötigt beim Akzeptieren eines Vorschlags.
     */
    public VorschlagService(VorschlagDAO vorschlagDAO,  BuchDAO buchDAO) {
        this.vorschlagDAO = vorschlagDAO;
        this.buchDAO = buchDAO;
    }

    /**
     * Erstellt einen neuen Buchvorschlag und speichert ihn in der Datenbank.
     *
     * @param titel  Der Titel des vorgeschlagenen Buches.
     * @param autor  Der Autor des vorgeschlagenen Buches.
     * @param nutzer Der Nutzer, der den Vorschlag einreicht.
     */
    public void buchVorschlagen(String titel, String autor, Nutzer nutzer) {
        Vorschlag vorschlag = new Vorschlag(0, titel, autor, VorschlagsStatus.OFFEN, nutzer, false);
        vorschlagDAO.save(vorschlag);
    }

    /**
     * Ruft eine Liste aller eingereichten Vorschläge ab.
     *
     * @return Eine Liste aller Vorschläge.
     */
    public List<Vorschlag> alleVorschlaege() {
        return vorschlagDAO.getAll();
    }

    /**
     * Findet alle Vorschläge eines Nutzers, über deren Statusänderung er noch nicht benachrichtigt wurde.
     *
     * @param nutzer Der Nutzer, für den die Benachrichtigungen geprüft werden sollen.
     * @return Eine Liste von Vorschlägen, die eine Benachrichtigung erfordern.
     */
    public List<Vorschlag> nichtBenachrichtigteNutzer(Nutzer nutzer) {
        return vorschlagDAO.findNichtBenachrichtigte(nutzer.getCustomerId());
    }

    /**
     * Markiert einen Vorschlag als "BESTELLT" und legt einen entsprechenden Bucheintrag an.
     *
     * @param id Die ID des zu akzeptierenden Vorschlags.
     * @throws CheckedException Wenn kein Vorschlag mit der ID gefunden wird oder dieser bereits bestellt wurde.
     */
    public void alsBestelltMarkieren(int id) throws CheckedException {
        Vorschlag vorschlag = vorschlagDAO.findById(id);
        if (vorschlag == null) {
            throw new CheckedException("❌ Vorschlag mit ID " + id + " nicht gefunden.");
        }
        if (vorschlag.getStatus() == VorschlagsStatus.BESTELLT) {
            throw new CheckedException("⚠️ Vorschlag ist bereits als bestellt markiert.");
        }
        vorschlag.setStatus(VorschlagsStatus.BESTELLT);
        vorschlagDAO.update(vorschlag);
        buchAnlegen(vorschlag);
    }

    /**
     * Private Hilfsmethode, die ein neues Buch-Objekt basierend auf einem akzeptierten Vorschlag erstellt.
     *
     * @param vorschlag Der akzeptierte Vorschlag.
     */
    private void buchAnlegen(Vorschlag vorschlag) {
        Buch buch = new Buch(vorschlag.getBuchTitel(), vorschlag.getAutor(), -1, true, false,
                 false, vorschlag.getNutzer());
        buchDAO.save(buch);
    }

    /**
     * Markiert einen Vorschlag als "benachrichtigt", sodass der Nutzer nicht erneut informiert wird.
     *
     * @param vorschlag Der Vorschlag, der als benachrichtigt markiert werden soll.
     */
    public void benachrichtigen(Vorschlag vorschlag) {
        vorschlag.setBenachrichtigt(true);
        vorschlagDAO.update(vorschlag);
    }

    /**
     * Markiert einen Vorschlag als "ABGELEHNT".
     *
     * @param id Die ID des abzulehnenden Vorschlags.
     * @throws CheckedException Wenn kein Vorschlag mit der angegebenen ID gefunden wird.
     */
    public void vorschlagAblehnen(int id) throws CheckedException {
        Vorschlag vorschlag = vorschlagDAO.findById(id);
        if (vorschlag == null) {
            throw new CheckedException("❌ Vorschlag mit ID " + id + " nicht gefunden.");
        }
        vorschlag.setStatus(VorschlagsStatus.ABGELEHNT);
        vorschlagDAO.update(vorschlag);
    }
}