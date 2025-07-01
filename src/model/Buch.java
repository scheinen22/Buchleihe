package model;

public class Buch {

    private String titel;
    private String author;
    private int bookId;
    private boolean available;
    private boolean rentingStatus;
    private boolean fernleihe;
    private Nutzer reserviertVonNutzer;

    /**
     * Tabelle Buch wird als Modelklasse abgebildet.
     * @param titel
     * @param author
     * @param bookId
     * @param available
     * @param rentingStatus
     * @param fernleihe
     */
    public Buch(String titel, String author, int bookId, boolean available, boolean rentingStatus, boolean fernleihe, Nutzer reserviertVonNutzer) {
        setTitel(titel);
        setAuthor(author);
        setBookId(bookId);
        setAvailable(available);
        setRentingStatus(rentingStatus);
        setFernleihe(fernleihe);
        setReserviertVonNutzer(reserviertVonNutzer);
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

    public Nutzer getReserviertVonNutzer() {
        return reserviertVonNutzer;
    }

    public void setReserviertVonNutzer(Nutzer reserviertVonNutzer) {
        this.reserviertVonNutzer = reserviertVonNutzer;
    }

    @Override
    public String toString() {
        return "\n----------------------" + "\nTitel: " + getTitel() + "\nAutor: " + getAuthor() +
                "\nISBN: " + bookId +
                "\nVerfügbarkeit im Lager: " + (available ? "✅" : "❌") + "\nAusgeliehen: " + (rentingStatus ? "✅" : "❌") +
                "\nZur Fernleihe verfügbar: " + (fernleihe ? "✅" : "❌") + "\n----------------------";
    }
}