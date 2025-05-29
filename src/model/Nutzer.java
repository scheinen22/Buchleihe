package model;

public class Nutzer {
    private String name;
    private String surname;
    private int customerId;
    private String benutzername;
    private String passwort;

    public Nutzer(String name, String surname, int customerId, String benutzername, String passwort) {
        setName(name);
        setSurname(surname);
        setCustomerId(customerId);
        setBenutzername(benutzername);
        setPasswort(passwort);
    }
    public Nutzer() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
// Nutzer kann ein model.Buch zur Neubeschaffung vorschlagen
    // -> Mitarbeiter entscheidet anschließend, ob das model.Buch neu beschafft wird
    // Nutzer erhält Information in allen fällen, was das Ergebnis der Anforderung sein wird
}
