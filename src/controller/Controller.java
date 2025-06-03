package controller;

import data.BuchDAO;
import data.NutzerDAO;
import data.VormerkerlisteDAO;
import data.VorschlagDAO;
import exception.CheckedException;
import model.Buch;
import model.Nutzer;
import model.Vorschlag;
import service.AusleiheService;
import service.NutzerService;
import service.VorschlagService;
import view.View;

import java.util.List;

public class Controller {

    private final AusleiheService ausleiheService = new AusleiheService(BuchDAO.getInstance(), VormerkerlisteDAO.getInstance());
    private final NutzerService nutzerService = new NutzerService(NutzerDAO.getInstance());
    private final VorschlagService vorschlagsService = new VorschlagService(VorschlagDAO.getInstance());
    private static final String UNGUELTIG_EINGABE = "⚠️ Ungültige Eingabe. Bitte erneut versuchen.";

    public static void main(String[] args) {
        nutzerErstellen(false);
        new Controller().start();
    }

    public void start() {
        View.ausgabe("\n--------------------------------------------");
        View.ausgabe("Willkommen im Bibliotheksmanagement-System");
        View.ausgabe("--------------------------------------------");

        pause(500);
        ladeAnimation("\nInitialisiere Login");
        pause(250);

        Nutzer nutzer = login();

        zeigeHauptmenue(nutzer);
    }

    private void zeigeHauptmenue(Nutzer nutzer) {
        while (true) {
            pause(1500);
            View.ausgabe("\n📚 Hauptmenü");
            View.ausgabe("------------------------");
            View.ausgabe("1. Buch suchen"); // AusleiheService // fertig
            View.ausgabe("2. Buch ausleihen"); // AusleiheService // fertig
            View.ausgabe("3. Buch zurückgeben"); // AusleiheService // fertig
            View.ausgabe("4. Buch zur Neubeschaffung vorschlagen"); // AusleiheService
            View.ausgabe("5. Mein Profil anzeigen"); // NutzerService // fertig

            pruefeMitarbeiterStatus(nutzer);

            View.ausgabe("0. Abmelden"); //fertig
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
                    else View.ausgabe(UNGUELTIG_EINGABE);
                }
                case 7 -> {
                    if (nutzer.isMitarbeiter()) {
                        buchBestellen();
                    }
                    else View.ausgabe(UNGUELTIG_EINGABE);
                }
                case 8 -> {
                    if (nutzer.isMitarbeiter()) {
                        nutzerVerwalten(nutzer);
                    }
                    else View.ausgabe(UNGUELTIG_EINGABE);
                }
                case 0 -> {
                    View.ausgabe("\n👋 Sie wurden abgemeldet.");
                    return;
                }
                default -> View.ausgabe(UNGUELTIG_EINGABE);
            }
        }
    }

    private void pruefeMitarbeiterStatus(Nutzer nutzer) {
        if (nutzer.isMitarbeiter()) {
            View.ausgabe("6. Vorschläge zur Neubeschaffung einsehen"); // MitarbeiterService // nicht fertig
            View.ausgabe("7. Buch als bestellt markieren"); // MitarbeiterService // nicht fertig
            View.ausgabe("8. Nutzer verwalten"); // MitarbeiterService // fertig
        }
    }

    private void buchVorschlagen(Nutzer nutzer) {
        View.ausgabe("\n📚 Buchvorschlag für Neubeschaffung");

        String titel = View.eingabe("Buchtitel: ");
        String autor = View.eingabe("Autor: ");

        if (titel.isBlank() || autor.isBlank()) {
            View.ausgabe("⚠️ Titel und Autor dürfen nicht leer sein.");
            return;
        }

        vorschlagsService.buchVorschlagen(titel, autor, nutzer);
        View.ausgabe("✅ Buchvorschlag wurde gespeichert. Vielen Dank!");
        View.pauseBisEnter();
    }

    private void buchBestellen() {
        View.ausgabe("\n📦 Vorschlag als bestellt markieren");
        View.ausgabe("Bitte geben Sie die ID des Vorschlags ein:");
        int id = View.eingabeInt();
        try {
            vorschlagsService.alsBestelltMarkieren(id);
            View.ausgabe("✅ Vorschlag wurde als bestellt markiert.");
        } catch (CheckedException e) {
            View.ausgabe(e.getMessage());
        }

        View.pauseBisEnter();
    }

    private void profilAnzeigen(Nutzer nutzer) {
        View.ausgabe("\nIhr Profil: ");
        nutzerService.profilAnzeigen(nutzer);
        View.pauseBisEnter();
    }

    private void vorschlaegeEinsehen() {
        View.ausgabe("\n📬 Vorschläge zur Neubeschaffung:");

        List<Vorschlag> liste = vorschlagsService.alleVorschlaege();
        if (liste.isEmpty()) {
            View.ausgabe("Keine Vorschläge vorhanden.");
            } else {
                for (Vorschlag v : liste) {
                    View.ausgabe(v.toString());
                }
            }
            View.pauseBisEnter();
    }


    private void nutzerVerwalten(Nutzer nutzer) {
        View.ausgabe("\n👤 Nutzerverwaltung");
        View.ausgabe("1. Alle Nutzer anzeigen");
        View.ausgabe("2. Nutzer löschen");
        View.ausgabe("3. Zurück zum Hauptmenü");
        View.ausgabe("------------------------");
        View.ausgabe("Bitte wählen Sie eine Option:");

        int auswahl = View.eingabeInt();
        switch (auswahl) {
            case 1 -> {
                List<Nutzer> nutzerList = nutzerService.alleNutzer();
                if (nutzerList.isEmpty()) {
                    View.ausgabe("Keine Nutzer gefunden.");
                } else {
                    View.ausgabe("Nutzerliste:");
                    for (Nutzer n : nutzerList) {
                        View.ausgabe(n.toString());
                    }
                }
                View.pauseBisEnter();
            }
            case 2 -> {
                View.ausgabe("Bitte geben Sie die ID des zu löschenden Nutzers an: ");
                int id = View.eingabeInt();
                try {
                    nutzerService.nutzerLoeschen(id);
                } catch (CheckedException e) {
                    View.ausgabe(e.getMessage());
                }
                View.pauseBisEnter();
            }
            case 3 -> View.ausgabe("Zurück zum Hauptmenü...");
            default -> View.ausgabe(UNGUELTIG_EINGABE);
        }
    }

    private void buchZurueckgeben(Nutzer nutzer) {
        View.ausgabe("📖 Buchrückgabe");
        pause(1000);
        View.ausgabe("\nBitte geben Sie die ISBN des Buchs ein: ");
        int isbn = View.eingabeInt();
        try {
            ausleiheService.rueckgabe(isbn, nutzer);
            View.ausgabe("Das Buch wurde erfolgreich zurückgegeben.");
        } catch (CheckedException e) {
            View.ausgabe(e.getMessage());
        }
    }

    private void ausleihen(Nutzer nutzer) {
        View.ausgabe("📖 Buchausleihe");
        pause(1000);
        View.ausgabe("Bitte geben Sie die ISBN des Buchs ein: ");
        int isbn = View.eingabeInt();
        try {
            ausleiheService.ausleihen(isbn, nutzer);
            View.ausgabe("\n✅ Das Buch wurde erfolgreich ausgeliehen.");
        } catch (CheckedException e) {
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

            Nutzer nutzer = nutzerService.authentifizieren(benutzername, passwort); // Schauen, ob in der DB Benutzernamen doppelt vorkommen können

            if (nutzer != null) {
                View.ausgabe("\n✅ Login erfolgreich. Willkommen, " + nutzer.getName() + " " + nutzer.getSurname() + "!");
                return nutzer;
            } else {
                View.ausgabe("❌ Login fehlgeschlagen. Benutzer nicht gefunden oder Passwort falsch.\n");
            }
        }
    }

    private void buchSuchen() {
        View.ausgabe("\n📖 Buchsuche");
        pause(500);
        View.ausgabe("Bitte geben Sie die ISBN des Buchs ein:");
        int isbn = View.eingabeInt();
        try {
            Buch buch = ausleiheService.sucheBuch(isbn);
            pause(500);
            View.ausgabe(buch.toString()); // Schöne toString() bauen am Ende!!!
        } catch (CheckedException e) {
            View.ausgabe(e.getMessage());
        }
        View.pauseBisEnter();
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
            pause(500);
            System.out.print(".");
        }
        System.out.println();
    }

    // Methode zum schnellen Anlegen von Testnutzern, wird später gelöscht
    private static void nutzerErstellen(boolean wahl) {
        if (wahl) {
            Nutzer nutzer = new Nutzer("ben", "KEIN MITARBEITER", 2, "kmit", "123", false);
            NutzerDAO nutzerDAO = NutzerDAO.getInstance();
            nutzerDAO.save(nutzer);
        }
    }
}
