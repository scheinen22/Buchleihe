package service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import data.BuchDAO;
import data.VormerkerlisteDAO;
import exception.CheckedException;
import model.Buch;
import model.Nutzer;
import model.Vormerkerliste;
import view.View;

public class AusleiheService {

    private final BuchDAO buchDAO;
    private final VormerkerlisteDAO vormerkerlisteDAO;

    public AusleiheService(BuchDAO buchDAO, VormerkerlisteDAO vormerkerlisteDAO) {
        this.buchDAO = buchDAO;
        this.vormerkerlisteDAO = vormerkerlisteDAO;
    }

    public Buch sucheBuch(int id) throws CheckedException {
        Buch buch = buchDAO.findById(id);
        if (buch == null) {
            throw new CheckedException("Kein Buch mit ID " + id + " gefunden.");
        }
        return buch;
    }

    public void ausleihen(int id, Nutzer nutzer) throws CheckedException {
        Objects.requireNonNull(nutzer);
        Buch buch = sucheBuch(id);
        if (buch.isAvailable() && !buch.isRentingStatus()) {
            List<Vormerkerliste> vormerker = vormerkerlisteDAO.findByBookIdSorted(buch.getBookId());
            if (!vormerker.isEmpty()) {
                Vormerkerliste ersterEintrag = vormerker.getFirst();
                if (ersterEintrag.getNutzer().getCustomerId() != nutzer.getCustomerId()) {
                    throw new CheckedException("❌ Das Buch ist vorgemerkt. Sie sind nicht an erster Stelle.");
                }
                View.ausgabe("🔁 Sie wurden aus der Vormerkerliste entfernt.");
                vormerkerlisteDAO.delete(ersterEintrag);
            }
            buch.setAusgeliehenAnNutzer(nutzer);
            buch.setAvailable(false);
            buch.setRentingStatus(true);
            buchDAO.update(buch);
        } else {
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

    public void rueckgabe(int id, Nutzer nutzer) throws CheckedException {
        Objects.requireNonNull(nutzer);
        Buch buch = sucheBuch(id);
        if (buch.isRentingStatus() && !buch.isAvailable()) {
            Nutzer aktuellerAusleiher = buch.getAusgeliehenAnNutzer();
            if (aktuellerAusleiher != null && aktuellerAusleiher.getCustomerId() == nutzer.getCustomerId()) {
                buch.setAusgeliehenAnNutzer(null);
                buch.setAvailable(true);
                buch.setRentingStatus(false);
                buchDAO.update(buch);
            } else {
                throw new CheckedException("❌ Sie können dieses Buch nicht zurückgeben. Sie haben es nicht ausgeliehen.");
            }
        } else {
            throw new CheckedException("❌ Sie können dieses Buch nicht zurückgeben. Es ist nicht verliehen");
        }
    }
}
