package service;

import data.BuchDAO;
import data.VorschlagDAO;
import exception.CheckedException;
import model.Buch;
import model.Nutzer;
import model.Vorschlag;
import model.VorschlagsStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Vorschlagservice enthält die Businesslogik für die Vorschläge.
 * In dieser Klasse wird mit den besorgten Objekten aus der Datenbank hantiert.
 */
public class VorschlagService {

    private final VorschlagDAO vorschlagDAO;
    private final BuchDAO buchDAO;
    private final Random rand = new Random();

    public VorschlagService(VorschlagDAO vorschlagDAO,  BuchDAO buchDAO) {
        this.vorschlagDAO = vorschlagDAO;
        this.buchDAO = buchDAO;
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
        buchAnlegen(vorschlag);
    }

    private void buchAnlegen(Vorschlag vorschlag) {
        Buch buch = new Buch(vorschlag.getBuchTitel(), vorschlag.getAutor(), -1, false, false,
                 true, vorschlag.getNutzer());
        buchDAO.save(buch);
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