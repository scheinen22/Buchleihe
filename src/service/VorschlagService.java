package service;

import data.VorschlagDAO;
import exception.CheckedException;
import model.Nutzer;
import model.Vorschlag;
import model.VorschlagsStatus;

import java.util.List;
import java.util.Objects;

/**
 * Vorschlagservice enthält die Businesslogik für die Vorschläge.
 * In dieser Klasse wird mit den besorgten Objekten aus der Datenbank hantiert.
 */
public class VorschlagService {

    private final VorschlagDAO vorschlagDAO;

    public VorschlagService(VorschlagDAO vorschlagDAO) {
        this.vorschlagDAO = vorschlagDAO;
    }

    public void buchVorschlagen(String titel, String autor, Nutzer nutzer) {
        Objects.requireNonNull(nutzer);
        Vorschlag vorschlag = new Vorschlag(0, titel, autor, VorschlagsStatus.OFFEN, nutzer, false);
        vorschlagDAO.save(vorschlag);
    }

    public List<Vorschlag> alleVorschlaege() {
        return vorschlagDAO.getAll();
    }

    public List<Vorschlag> nichtBenachrichtigteNutzer(Nutzer nutzer) {
        Objects.requireNonNull(nutzer);
        return vorschlagDAO.findNichtBenachrichtigte(nutzer.getCustomerId());
    }

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
    }

    public void benachrichtigen(Vorschlag vorschlag) {
        Objects.requireNonNull(vorschlag);
        vorschlag.setBenachrichtigt(true);
        vorschlagDAO.update(vorschlag);
    }

    public void vorschlagAblehnen(int id) throws CheckedException {
        Vorschlag vorschlag = vorschlagDAO.findById(id);
        if (vorschlag == null) {
            throw new CheckedException("❌ Vorschlag mit ID " + id + " nicht gefunden.");
        }
        vorschlag.setStatus(VorschlagsStatus.ABGELEHNT);
        vorschlagDAO.update(vorschlag);
    }
}