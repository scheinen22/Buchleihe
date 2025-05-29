package service;

import data.BuchDAO;
import exception.BuchNichtGefundenException;
import model.Buch;

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
}
