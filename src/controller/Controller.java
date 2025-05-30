package controller;

import data.BuchDAO;
import data.NutzerDAO;
import data.VormerkerlisteDAO;
import exception.BuchBereitsVerliehenException;
import exception.BuchNichtGefundenException;
import model.Buch;
import model.Nutzer;
import service.AusleiheService;
import service.AuthentifizierungService;
import view.View;

public class Controller {
    private final AusleiheService ausleiheService = new AusleiheService(BuchDAO.getInstance(), VormerkerlisteDAO.getInstance());

    public static void main(String[] args) {
        nutzerErstellen(false);
        new Controller().start();
    }
    public void start() {
        View.ausgabe("--------------------------------------------");
        View.ausgabe("Willkommen im Bibliotheksmanagement-System");
        View.ausgabe("--------------------------------------------");

        ladeAnimation("Initialisiere Login");
        pause(250);

        Nutzer nutzer = login();

        zeigeHauptmenue(nutzer);
    }
    private void zeigeHauptmenue(Nutzer nutzer) {
        while (true) {
            View.ausgabe("\n📚 Hauptmenü");
            View.ausgabe("------------------------");
            View.ausgabe("1. Buch suchen");
            View.ausgabe("2. Buch ausleihen");
            View.ausgabe("3. Buch zurückgeben");
            View.ausgabe("4. Buch zur Neubeschaffung vorschlagen");
            View.ausgabe("5. Mein Profil anzeigen");

            if (nutzer.isMitarbeiter()) {
                View.ausgabe("6. Vorschläge zur Neubeschaffung einsehen");
                View.ausgabe("7. Buch als bestellt markieren");
                View.ausgabe("8. Nutzer verwalten");
            }

            View.ausgabe("0. Abmelden");
            View.ausgabe("------------------------");
            View.ausgabe("Bitte wählen Sie eine Option:");

            int auswahl = View.eingabeInt();

            switch (auswahl) {
                case 1 -> buchSuchen(); // -> Infos abfragen, dann für die Logik und Zugriff auf die DAOs in die Services leiten
                case 2 -> ausleihen(nutzer);
                case 3 -> buchZurueckgeben(nutzer);
                case 4 -> buchVorschlagen(nutzer);
                case 5 -> profilAnzeigen(nutzer);
                case 6 -> {
                    if (nutzer.isMitarbeiter()) {
                        vorschlaegeEinsehen();
                    }
                    else View.ausgabe("⛔ Zugriff verweigert.");
                }
                case 7 -> {
                    if (nutzer.isMitarbeiter()) {
                        buchBestellen();
                    }
                    else View.ausgabe("⛔ Zugriff verweigert.");
                }
                case 8 -> {
                    if (nutzer.isMitarbeiter()) {
                        nutzerVerwalten();
                    }
                    else View.ausgabe("⛔ Zugriff verweigert.");
                }
                case 0 -> {
                    View.ausgabe("\n👋 Sie wurden abgemeldet.");
                    return;
                }
                default -> View.ausgabe("⚠️ Ungültige Eingabe. Bitte erneut versuchen.");
            }
        }
    }

    private void buchZurueckgeben(Nutzer nutzer) {
        View.ausgabe("📖 Buchrückgabe");
        pause(1000);
        View.ausgabe("\"Bitte geben Sie die ISBN des Buchs ein: \"");
        int isbn = View.eingabeInt();
        try {
            ausleiheService.rueckgabe(isbn, nutzer);
            View.ausgabe("Das Buch wurde erfolgreich zurückgegeben.");
        } catch (BuchNichtGefundenException | BuchBereitsVerliehenException e) {
            View.ausgabe(e.getMessage());
        }
    }

    private void ausleihen(Nutzer nutzer) {
        View.ausgabe("📖 Buchausleihe");
        pause(1000);
        View.ausgabe("\"Bitte geben Sie die ISBN des Buchs ein: \"");
        int isbn = View.eingabeInt();
        try {
            ausleiheService.ausleihen(isbn, nutzer);
            View.ausgabe("Das Buch wurde erfolgreich ausgeliehen.");
        } catch (BuchNichtGefundenException | BuchBereitsVerliehenException e) {
            View.ausgabe(e.getMessage());
        }

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
    private void buchSuchen() {
        View.ausgabe("📖 Buchsuche");
        pause(1000);
        View.ausgabe("\"Bitte geben Sie die ISBN des Buchs ein: \"");
        int isbn = View.eingabeInt();
        try {
            Buch buch = ausleiheService.sucheBuch(isbn);
            View.ausgabe(buch.toString());
        } catch (BuchNichtGefundenException e) {
            View.ausgabe(e.getMessage());
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
    private static void nutzerErstellen(boolean wahl) {
        if (wahl) {
            Nutzer nutzer = new Nutzer("ben", "johann", 1, "said", "123", true);
            NutzerDAO nutzerDAO = NutzerDAO.getInstance();
            nutzerDAO.update(nutzer);
        }
    }
}
