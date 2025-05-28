package model;

public class Buch {
    private String titel;
    private String author;
    private int bookId;
    private boolean available;
    private boolean rentingStatus;

    public Buch(String titel, String author, int bookId, boolean available, boolean rentingStatus) {
        this.titel = titel;
        this.author = author;
        this.bookId = bookId;
        this.available = true;
        this.rentingStatus = false;
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
// model.Buch kann per Fernleihe beschafft werden
    // Es muss zwischen Fern- und Ortsleihe unterschieden werden
    // Wenn model.Buch neu beschafft wird, wird es im Katalog verzeichnet, als bestellt markiert und für den Kunden reserviert werden
    // Frage: Was sind Bücher die per Fernleihe angefordert werden? Bücher, die die Bib hat und per post verleihen kann / Bücher, die die Bib nicht hat und nachbestellen muss

}