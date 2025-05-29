package model;

import java.sql.Date;

public class Vormerkerliste {
    private Nutzer nutzer;
    private Buch buch;
    private Date eintrittsDatum;

    public Vormerkerliste(Nutzer nutzer, Buch buch, Date eintrittsDatum) {
        setNutzer(nutzer);
        setBuch(buch);
        setEintrittsDatum(eintrittsDatum);
    }

    public Date getEintrittsDatum() {
        return eintrittsDatum;
    }

    public void setEintrittsDatum(Date eintrittsDatum) {
        this.eintrittsDatum = eintrittsDatum;
    }

    public Buch getBuch() {
        return buch;
    }

    public void setBuch(Buch buch) {
        this.buch = buch;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }


}
