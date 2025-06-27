package service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import data.AusleiheDAO;
import data.BuchDAO;
import data.VormerkerlisteDAO;
import exception.CheckedException;
import model.Ausleihe;
import model.Buch;
import model.Nutzer;
import model.Vormerkerliste;
import view.View;

public class AusleiheService {

    private final BuchDAO buchDAO;
    private final VormerkerlisteDAO vormerkerlisteDAO;
    private final AusleiheDAO ausleiheDAO;

    public AusleiheService(BuchDAO buchDAO, VormerkerlisteDAO vormerkerlisteDAO, AusleiheDAO ausleiheDAO) {
        this.buchDAO = buchDAO;
        this.vormerkerlisteDAO = vormerkerlisteDAO;
        this.ausleiheDAO = ausleiheDAO;
    }

    public Buch sucheBuch(int id) throws CheckedException {
        Buch buch = buchDAO.findById(id);
        if (buch == null) {
            throw new CheckedException("Kein Buch mit ID " + id + " gefunden.");
        }
        return buch;
    }

    public void ausleihen(int buchId, Nutzer nutzer) throws CheckedException {
        Buch buch = sucheBuch(buchId);
        List<Ausleihe> alleAusleihen = ausleiheDAO.findAlleBuecherByBuchIdUndOffen(buchId);
        // Schaue, ob die Liste null ist
        List<Ausleihe> sichereAusleihen = (alleAusleihen != null) ? alleAusleihen : List.of();
        List<Ausleihe> gefiltertNachNutzer = sichereAusleihen.stream()
                .filter(a -> a.getNutzer().getCustomerId() == nutzer.getCustomerId())
                .toList();
        // Fernleihe: erlaubt parallele Ausleihen, aber nur wenn das Buch schon verliehen ist.
        if (buch.isFernleihe() && !sichereAusleihen.isEmpty() && gefiltertNachNutzer.isEmpty()) {
            Ausleihe fernleihe = new Ausleihe(buch, nutzer, Date.valueOf(LocalDate.now()), true);
            ausleiheDAO.save(fernleihe);
            View.ausgabe("📦 Hinweis: Dieses Buch wird als Fernleihe bereitgestellt.");
            return;
        }
        // Aktuelle Ausleihen werden präpariert, Fernleihen entfernt
        List<Ausleihe> vollgefilterteAusleihen = sichereAusleihen.stream()
                .filter(a -> a.getRueckgabedatum() == null && !a.isFernleihe() && buch.isAvailable() && !buch.isRentingStatus())
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

    private void vormerken(Nutzer nutzer, Buch buch) {
        if (vormerkerlisteDAO.istSchonVorgemerkt(buch.getBookId(), nutzer.getCustomerId())) {
            View.ausgabe("⚠️ Sie stehen bereits auf der Vormerkerliste für dieses Buch.");
            return;
        }
        Vormerkerliste vormerker = new Vormerkerliste(nutzer, buch, Date.valueOf(LocalDate.now()));
        vormerkerlisteDAO.save(vormerker);
        View.ausgabe("📝 Sie wurden erfolgreich auf die Vormerkerliste gesetzt.");
    }

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

    public List<Buch> holeAlleBuecher() {
        return buchDAO.getAll();
    }
}
