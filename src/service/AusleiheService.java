package service;

import java.sql.Date;
import java.time.LocalDate;

import data.BuchDAO;
import data.VormerkerlisteDAO;
import exception.BuchBereitsVerliehenException;
import exception.BuchNichtGefundenException;
import model.Buch;
import model.Nutzer;
import model.Vormerkerliste;
import view.View;

public class AusleiheService {

    private final BuchDAO buchDAO;
    private final VormerkerlisteDAO vormerkerlisteDAO;

    public AusleiheService(BuchDAO buchDAO, VormerkerlisteDAO vormerkerlisteDAO) {
        this.buchDAO = buchDAO;
        this.vormerkerlisteDAO = vormerkerlisteDAO;
    }

    public Buch sucheBuch(int id) {
        Buch buch = buchDAO.findById(id);
        if (buch == null) {
            throw new BuchNichtGefundenException("Kein Buch mit ID " + id + " gefunden.");
        }
        return buch;
    }

    public void ausleihen(int id, Nutzer nutzer) throws BuchBereitsVerliehenException {
        Buch buch = sucheBuch(id);
        if (buch.isAvailable() && !buch.isRentingStatus()) {
            buch.setAusgeliehenAnNutzer(nutzer);
            buch.setAvailable(false);
            buch.setRentingStatus(true);
            buchDAO.update(buch);
        } else {
            vormerken(nutzer, buch);
            throw new BuchBereitsVerliehenException("Das Buch ist bereits verliehen!");
        }
    }

    private void vormerken(Nutzer nutzer, Buch buch) {
        if (vormerkerlisteDAO.istSchonVorgemerkt(buch.getBookId(), nutzer.getCustomerId())) {
            View.ausgabe("⚠️ Sie stehen bereits auf der Vormerkerliste für dieses Buch.");
            return;
        }
        Vormerkerliste vormerker = new Vormerkerliste(nutzer, buch, Date.valueOf(LocalDate.now()));
        vormerkerlisteDAO.save(vormerker);
        View.ausgabe("📝 Sie wurden erfolgreich auf die Vormerkerliste gesetzt.");
    }


    public void rueckgabe(int id, Nutzer nutzer) throws BuchBereitsVerliehenException {
        Buch buch = sucheBuch(id);
        if (buch.isRentingStatus() && !buch.isAvailable()) {
            buch.setAusgeliehenAnNutzer(null);
            buch.setAvailable(true);
            buch.setRentingStatus(false);
            buchDAO.update(buch);
        } else {
            throw new BuchBereitsVerliehenException("Das Buch ist bereits verliehen!");
        }
    }
}
