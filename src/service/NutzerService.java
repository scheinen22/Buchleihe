package service;

import data.NutzerDAO;
import model.Nutzer;

public class NutzerService {

    private final NutzerDAO nutzerDAO;

    public NutzerService(NutzerDAO nutzerDAO) {
        this.nutzerDAO = nutzerDAO;
    }

    public Nutzer authentifizieren(String benutzername, String passwort) {
        return nutzerDAO.findByBenutzernameUndPasswort(benutzername, passwort);
    }
}
