package service;

import data.NutzerDAO;
import model.Nutzer;

public class AuthentifizierungService {

    public static Nutzer authentifizieren(String benutzername, String passwort) {
        NutzerDAO nutzerDAO = NutzerDAO.getInstance();
        return nutzerDAO.findByBenutzernameUndPasswort(benutzername, passwort);
    }
}
