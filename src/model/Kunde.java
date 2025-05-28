package model;

public class Kunde {
    private String name;
    private String surname;
    private int customerId;

    public Kunde(String name, String surname, int customerId) {
        this.name = name;
        this.surname = surname;
        this.customerId = customerId;
    }

    // Nutzer kann ein model.Buch zur Neubeschaffung vorschlagen
    // -> Mitarbeiter entscheidet anschließend, ob das model.Buch neu beschafft wird
    // Nutzer erhält Information in allen fällen, was das Ergebnis der Anforderung sein wird
}
