package view;

import java.util.Scanner;

/**
 * Die Klasse View ist eine Hilfsklasse zur Verarbeitung von Konsoleneingaben und -ausgaben.
 */
public class View {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Konstruktor als private gesetzt, da alle Methoden static gesetzt sind
     * und somit nicht aus einem erzeugten View Objekt aufgerufen werden sollen.
     */
    private View() {
        throw new IllegalStateException("Utility class");
    }

    public static void ausgabe(String text) {
        System.out.println(text);
    }

    public static String eingabe(String text) {
        ausgabe(text);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static int eingabeInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                ausgabe("Bitte eine gültige Ganzzahl eingeben: ");
            }
        }
    }

    public static void pauseBisEnter() {
        System.out.print("\n🔁 Drücken Sie ENTER, um fortzufahren...");
        scanner.nextLine();
    }
}
