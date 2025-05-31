package service;

import data.NutzerDAO;
import exception.NutzerNichtGefundenException;
import model.Nutzer;
import view.View;

import java.util.List;

public class NutzerService {

    private final NutzerDAO nutzerDAO;

    public NutzerService(NutzerDAO nutzerDAO) {
        this.nutzerDAO = nutzerDAO;
    }

    public Nutzer authentifizieren(String benutzername, String passwort) {
        return nutzerDAO.findByBenutzernameUndPasswort(benutzername, passwort);
    }

    public List<Nutzer> alleNutzer() {
        return nutzerDAO.getAll();
    }

    public void nutzerLoeschen(int id) throws NutzerNichtGefundenException {
        Nutzer nutzer = nutzerDAO.findById(id);
        if (nutzer != null) {
            nutzerDAO.delete(nutzer);
            View.ausgabe("Nutzer wurde gelöscht!");
        } else {
            View.ausgabe("Nutzer existiert nicht.");
        }
    }
}
