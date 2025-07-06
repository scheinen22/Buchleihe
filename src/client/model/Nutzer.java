package client.model;

import java.io.Serializable;

public class Nutzer implements Serializable {

    private String name;
    private String surname;
    private int customerId;
    private String benutzername;
    private String passwort;
    private boolean mitarbeiterStatus;

    public Nutzer(String name, String surname, int customerId, String benutzername, String passwort, boolean mitarbeiterStatus) {
        setName(name);
        setSurname(surname);
        setCustomerId(customerId);
        setBenutzername(benutzername);
        setPasswort(passwort);
        setMitarbeiterStatus(mitarbeiterStatus);
    }

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

    public boolean isMitarbeiter() {
        return mitarbeiterStatus;
    }

    public void setMitarbeiterStatus(boolean mitarbeiterStatus) {
        this.mitarbeiterStatus = mitarbeiterStatus;
    }

    @Override
    public String toString() {
        return "----------------------\n" + "Name: " + name + " \nNachname:" + surname + "\nKunden ID:" + customerId + "\nBenutzername: "
                + benutzername + "\n----------------------";
    }

}
