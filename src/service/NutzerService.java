package service;

import data.NutzerDAO;
import exception.CheckedException;
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

    public void nutzerLoeschen(int id) throws CheckedException {
        Nutzer nutzer = nutzerDAO.findById(id);
        if (nutzer != null && nutzer.getCustomerId() == id) {
            nutzerDAO.delete(nutzer);
            View.ausgabe("Nutzer wurde gelöscht!");
        } else {
            throw new CheckedException("Nutzer existiert nicht.");
        }
    }

    public void profilAnzeigen(Nutzer nutzer) {
        View.ausgabe(nutzer.toString());
    }
}
