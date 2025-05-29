package controller;

import data.NutzerDAO;
import model.Nutzer;
import service.AuthentifizierungService;
import view.View;

public class Controller {
    public static void main(String[] args) {
        nutzererstellen(false);
        new Controller().start();
    }
    public void start() {
        View.ausgabe("--------------------------------------------");
        View.ausgabe("Willkommen im Bibliotheksmanagement-System");
        View.ausgabe("--------------------------------------------");

        ladeAnimation("Initialisiere Login");
        pause(250);

        Nutzer nutzer = login();

    }

    private Nutzer login() {
        while (true) {
            View.ausgabe("\n🔐 Anmeldung erforderlich");

            String benutzername = View.eingabe("Benutzername: ").trim();
            String passwort = View.eingabe("Passwort: ").trim();

            if (benutzername.isEmpty() || passwort.isEmpty()) {
                View.ausgabe("⚠️ Benutzername und Passwort dürfen nicht leer sein.\n");
                continue;
            }

            Nutzer nutzer = AuthentifizierungService.authentifizieren(benutzername, passwort);

            if (nutzer != null) {
                View.ausgabe("\n✅ Login erfolgreich. Willkommen, " + nutzer.getName() + " " + nutzer.getSurname() + "!");
                return nutzer;
            } else {
                View.ausgabe("❌ Login fehlgeschlagen. Benutzer nicht gefunden oder Passwort falsch.\n");
            }
        }
    }
    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // gute Praxis
        }
    }
    private void ladeAnimation(String message) {
        View.ausgabe(message);
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
                System.out.print(".");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
    private static void nutzererstellen(boolean wahl) {
        if (wahl) {
            Nutzer nutzer = new Nutzer("ben", "johann", 1, "said", "123");
            NutzerDAO nutzerDAO = NutzerDAO.getInstance();
            nutzerDAO.save(nutzer);
        }
    }
}
