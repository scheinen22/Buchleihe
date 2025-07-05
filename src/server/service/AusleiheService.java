package server.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import client.exception.CheckedException;
import client.model.Ausleihe;
import client.model.Buch;
import client.model.Nutzer;
import client.model.Vormerkerliste;
import server.data.AusleiheDAO;
import server.data.BuchDAO;
import server.data.VormerkerlisteDAO;
import client.view.View;

/**
 * Der AusleiheService enthält die gesamte Geschäftslogik für Prozesse rund um die Ausleihe,
 * Rückgabe und Suche von Büchern. Er koordiniert die Interaktionen zwischen den zugehörigen DAOs
 * sowie dem Controller.
 */
public class AusleiheService {

    private final BuchDAO buchDAO;
    private final VormerkerlisteDAO vormerkerlisteDAO;
    private final AusleiheDAO ausleiheDAO;

    /**
     * Erstellt eine neue Instanz des AusleiheService. Stichwort Dependency Injection.
     *
     * @param buchDAO           Das DAO für den Zugriff auf Buch-Daten.
     * @param vormerkerlisteDAO Das DAO für den Zugriff auf die Vormerkerliste.
     * @param ausleiheDAO       Das DAO für den Zugriff auf Ausleih-Daten.
     */
    public AusleiheService(BuchDAO buchDAO, VormerkerlisteDAO vormerkerlisteDAO, AusleiheDAO ausleiheDAO) {
        this.buchDAO = buchDAO;
        this.vormerkerlisteDAO = vormerkerlisteDAO;
        this.ausleiheDAO = ausleiheDAO;
    }

    /**
     * Sucht ein Buch anhand seiner eindeutigen ID.
     *
     * @param id Die ID (ISBN) des zu suchenden Buches.
     * @return Das gefundene Buch-Objekt.
     * @throws CheckedException Wenn kein Buch mit der angegebenen ID gefunden wird.
     */
    public Buch sucheBuch(int id) throws CheckedException {
        Buch buch = buchDAO.findById(id);
        if (buch == null) {
            throw new CheckedException("Kein Buch mit ID " + id + " gefunden.");
        }
        return buch;
    }

    /**
     * Führt den Ausleihvorgang für ein Buch durch einen Nutzer aus.
     * Die Methode prüft Verfügbarkeit, Reservierungen, Vormerkungen und den Sonderfall der Fernleihe.
     *
     * @param buchId Die ID des auszuleihenden Buches.
     * @param nutzer Der Nutzer, der das Buch ausleihen möchte.
     * @throws CheckedException Wenn das Buch aus einem fachlichen Grund nicht ausgeliehen werden kann.
     */
    public void ausleihen(int buchId, Nutzer nutzer) throws CheckedException {
        Buch buch = sucheBuch(buchId);
        List<Ausleihe> alleAusleihen = ausleiheDAO.findAlleBuecherByBuchIdUndOffen(buchId);
        // Schaue, ob die Liste null ist
        List<Ausleihe> sichereAusleihen = (alleAusleihen != null) ? alleAusleihen : List.of();
        List<Ausleihe> gefiltertNachNutzer = sichereAusleihen.stream()
                .filter(a -> a.getNutzer().getCustomerId() == nutzer.getCustomerId())
                .toList();
        List<Ausleihe> fernleiheRausgefiltert = sichereAusleihen.stream()
                .filter(ausleihe -> !ausleihe.isFernleihe())
                .toList();
        // Fernleihe: erlaubt parallele Ausleihen, aber nur wenn das Buch schon verliehen ist.
        if (buch.isFernleihe() && !sichereAusleihen.isEmpty() && gefiltertNachNutzer.isEmpty() && !fernleiheRausgefiltert.isEmpty()) {
            Ausleihe fernleihe = new Ausleihe(buch, nutzer, Date.valueOf(LocalDate.now()), true);
            ausleiheDAO.save(fernleihe);
            View.ausgabe("📦 Hinweis: Dieses Buch wird als Fernleihe bereitgestellt.");
            return;
        }
        // Aktuelle Ausleihen werden präpariert, Fernleihen entfernt
        List<Ausleihe> vollgefilterteAusleihen = fernleiheRausgefiltert.stream()
                .filter(a -> a.getRueckgabedatum() == null)
                .toList();
        if (vollgefilterteAusleihen.isEmpty()) {
            // Nutzer hat das Buch reserviert → darf ausleihen
            if (buch.getReserviertVonNutzer() != null && buch.getReserviertVonNutzer().getCustomerId() == nutzer.getCustomerId()) {
                // Reservierung einlösen
                buch.setReserviertVonNutzer(null);
                Ausleihe neueAusleihe = new Ausleihe(buch, nutzer, Date.valueOf(LocalDate.now()), false);
                ausleiheDAO.save(neueAusleihe);
                buch.setAvailable(false);
                buch.setRentingStatus(true);
                buchDAO.update(buch);
                return;
            }
            // Kein Anspruch → Prüfe Vormerkerliste
            if (buch.getReserviertVonNutzer() == null) {
                List<Vormerkerliste> vormerker = vormerkerlisteDAO.findByBookIdSorted(buchId);
                if (!vormerker.isEmpty()) {
                    Vormerkerliste ersterEintrag = vormerker.getFirst();
                    if (ersterEintrag.getNutzer().getCustomerId() != nutzer.getCustomerId()) {
                        throw new CheckedException("❌ Das Buch ist bereits vorgemerkt.");
                    }
                    View.ausgabe("🔁 Sie wurden aus der Vormerkerliste entfernt.");
                    vormerkerlisteDAO.delete(ersterEintrag);
                }
                Ausleihe neueAusleihe = new Ausleihe(buch, nutzer, Date.valueOf(LocalDate.now()), false);
                ausleiheDAO.save(neueAusleihe);
                buch.setAvailable(false);
                buch.setRentingStatus(true);
                buchDAO.update(buch);
            }
            if (buch.getReserviertVonNutzer() != null) {
                throw new CheckedException("❌ Das Buch ist aktuell von jemandem reserviert.");
            }
        } else {
            if (gefiltertNachNutzer.stream().anyMatch(a -> a.getNutzer().getCustomerId() == nutzer.getCustomerId())) {
                throw new CheckedException("❌ Das Buch wurde bereits von dir ausgeliehen!");
            }
            vormerken(nutzer, buch);
            throw new CheckedException("❌ Das Buch ist bereits verliehen!");
        }
    }

    /**
     * Private Hilfsmethode, um einen Nutzer auf die Vormerkerliste für ein Buch zu setzen.
     * Prüft, ob der Nutzer bereits vorgemerkt ist.
     *
     * @param nutzer Der vorzumerkende Nutzer.
     * @param buch   Das Buch, für das vorgemerkt wird.
     */
    private void vormerken(Nutzer nutzer, Buch buch) {
        if (vormerkerlisteDAO.istSchonVorgemerkt(buch.getBookId(), nutzer.getCustomerId())) {
            View.ausgabe("⚠️ Sie stehen bereits auf der Vormerkerliste für dieses Buch.");
            return;
        }
        Vormerkerliste vormerker = new Vormerkerliste(nutzer, buch, Date.valueOf(LocalDate.now()));
        vormerkerlisteDAO.save(vormerker);
        View.ausgabe("📝 Sie wurden erfolgreich auf die Vormerkerliste gesetzt.");
    }

    /**
     * Verarbeitet die Rückgabe eines Buches durch einen Nutzer.
     *
     * @param buchId Die ID des zurückgegebenen Buches.
     * @param nutzer Der Nutzer, der das Buch zurückgibt.
     * @throws CheckedException Wenn der Nutzer das Buch nicht ausgeliehen hat oder es gar nicht verliehen ist.
     */
    public void rueckgabe(int buchId, Nutzer nutzer) throws CheckedException {
        Buch buch = sucheBuch(buchId);
        List<Ausleihe> offeneAusleihen = ausleiheDAO.findAlleBuecherByBuchIdUndOffen(buchId);
        if (offeneAusleihen == null || offeneAusleihen.isEmpty()) {
            throw new CheckedException("❌ Das Buch ist aktuell nicht verliehen.");
        }
        Ausleihe nutzerAusleihe = offeneAusleihen.stream()
                .filter(a -> a.getNutzer().getCustomerId() == nutzer.getCustomerId())
                .findFirst()
                .orElse(null);
        if (nutzerAusleihe == null) {
            throw new CheckedException("❌ Sie können dieses Buch nicht zurückgeben. Sie haben es nicht ausgeliehen.");
        }
        nutzerAusleihe.setRueckgabedatum(Date.valueOf(LocalDate.now()));
        ausleiheDAO.update(nutzerAusleihe);
        if (!nutzerAusleihe.isFernleihe()) {
            // Prüft, ob es weitere Ausleihen zu demselben Buch gibt. Zur Sicherheit drin lassen, um inkonsistente Datensätze zu vermeiden.
            boolean weitereNormaleAusleihen = offeneAusleihen.stream()
                    .anyMatch(a -> a.getId() != nutzerAusleihe.getId() && !a.isFernleihe());
            if (!weitereNormaleAusleihen) {
                buch.setAvailable(true);
                buch.setRentingStatus(false);
                buchDAO.update(buch);
            }
        }
    }

    /**
     * Ruft eine Liste aller Bücher im Katalog ab.
     *
     * @return Eine Liste aller Bücher.
     */
    public List<Buch> holeAlleBuecher() {
        return buchDAO.getAll();
    }
}
