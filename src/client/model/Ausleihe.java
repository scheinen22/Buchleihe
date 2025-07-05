package client.model;

import java.sql.Date;

public class Ausleihe {

    private int id;
    private Buch buch;
    private Nutzer nutzer;
    private Date ausleihdatum;
    private Date rueckgabedatum;
    private boolean fernleihe;

    public Ausleihe(int id, Buch buch, Nutzer nutzer, Date ausleihdatum, Date rueckgabedatum, boolean fernleihe) {
        setId(id);
        setBuch(buch);
        setNutzer(nutzer);
        setAusleihdatum(ausleihdatum);
        setRueckgabedatum(rueckgabedatum);
        setFernleihe(fernleihe);
    }

    public Ausleihe(Buch buch, Nutzer nutzer, Date ausleihdatum, boolean fernleihe) {
        this(-1, buch, nutzer, ausleihdatum, null, fernleihe); // Auto increment in der datenbank kümmert sich drum
    }

    public Buch getBuch() {
        return buch;
    }

    public void setBuch(Buch buch) {
        this.buch = buch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    public Date getAusleihdatum() {
        return ausleihdatum;
    }

    public void setAusleihdatum(Date ausleihdatum) {
        this.ausleihdatum = ausleihdatum;
    }

    public Date getRueckgabedatum() {
        return rueckgabedatum;
    }

    public void setRueckgabedatum(Date rueckgabedatum) {
        this.rueckgabedatum = rueckgabedatum;
    }

    public boolean isFernleihe() {
        return fernleihe;
    }

    public void setFernleihe(boolean fernleihe) {
        this.fernleihe = fernleihe;
    }
}