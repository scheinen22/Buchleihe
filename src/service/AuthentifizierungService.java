package service;

import data.NutzerDAO;
import model.Nutzer;

public class AuthentifizierungService {
    private AuthentifizierungService() {
        throw new IllegalStateException("Utility class");
    }
    public static Nutzer authentifizieren(String benutzername, String passwort) {
        NutzerDAO nutzerDAO = NutzerDAO.getInstance();
        return nutzerDAO.findByBenutzernameUndPasswort(benutzername, passwort);
    }
}
