package model;

public class Buch {

    private String titel;
    private String author;
    private int bookId;
    private boolean available;
    private boolean rentingStatus;
    private Nutzer ausgeliehenAnNutzer;
    private boolean fernleihe;

    public Buch(String titel, String author, int bookId, boolean available, boolean rentingStatus, Nutzer ausgeliehenAnNutzer, boolean fernleihe) {
        setTitel(titel);
        setAuthor(author);
        setBookId(bookId);
        setAvailable(available);
        setRentingStatus(rentingStatus);
        setAusgeliehenAnNutzer(ausgeliehenAnNutzer);
        setFernleihe(fernleihe);
    }

    public Nutzer getAusgeliehenAnNutzer() {
        return ausgeliehenAnNutzer;
    }

    public void setAusgeliehenAnNutzer(Nutzer ausgeliehenAnNutzer) {
        this.ausgeliehenAnNutzer = ausgeliehenAnNutzer;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean isRentingStatus() {
        return rentingStatus;
    }

    public void setRentingStatus(boolean rentingStatus) {
        this.rentingStatus = rentingStatus;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public boolean isFernleihe() {
        return fernleihe;
    }

    public void setFernleihe(boolean fernleihe) {
        this.fernleihe = fernleihe;
    }
    @Override
    //TODO: Bei den Zuständen eine Prüfung einbauen, und dann die false und true werte auf sprache umbiegen
    public String toString() {
        return "\n----------------------" + "\nTitel: " + getTitel() + "\nAutor: " + getAuthor() +
                "\nVerfügbarkeit im Lager: " + isAvailable() + "\nAusleihstatus: " + isRentingStatus() +
                "\nFernleihe: " + isFernleihe() + "\n----------------------";
    }
}