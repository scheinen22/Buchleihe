package service;

import data.BuchDAO;
import exception.BuchBereitsVerliehenException;
import exception.BuchNichtGefundenException;
import model.Buch;
import model.Nutzer;

public class AusleiheService {

    private final BuchDAO buchDAO;

    public AusleiheService(BuchDAO buchDAO) {
        this.buchDAO = buchDAO;
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
            throw new BuchBereitsVerliehenException("Das Buch ist bereits verliehen!");
        }

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
