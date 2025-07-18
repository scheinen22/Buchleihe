package client.model;

import java.io.Serializable;

public class Vorschlag implements Serializable {
    private int id;
    private String buchTitel;
    private String autor;
    private VorschlagsStatus status;
    private Nutzer nutzer;
    private boolean benachrichtigt;

    public Vorschlag(int id, String buchTitel, String autor, VorschlagsStatus status, Nutzer nutzer, boolean benachrichtigt) {
        setId(id);
        setBuchTitel(buchTitel);
        setAutor(autor);
        setStatus(status);
        setNutzer(nutzer);
        setBenachrichtigt(benachrichtigt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBenachrichtigt() {
        return benachrichtigt;
    }

    public void setBenachrichtigt(boolean benachrichtigt) {
        this.benachrichtigt = benachrichtigt;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    public VorschlagsStatus getStatus() {
        return status;
    }

    public void setStatus(VorschlagsStatus status) {
        this.status = status;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getBuchTitel() {
        return buchTitel;
    }

    public void setBuchTitel(String buchTitel) {
        this.buchTitel = buchTitel;
    }

    public String toString() {
        return "\n----------------------" + "\nTitel: " + getBuchTitel() + "\nAutor: " + getAutor() +
                "\nVorschlagsnummer: " + id +
                "\nVorgeschlagen von Nutzer ID: " + getNutzer().getCustomerId() +
                "\nStatus: " + getStatus() +
                "\n----------------------";
    }
}
