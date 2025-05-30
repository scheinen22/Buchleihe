package view;

import java.util.Scanner;

public class View {

    private static final Scanner scanner = new Scanner(System.in);

    private View() {
        throw new IllegalStateException("Utility class");
    }

    public static void ausgabe(String text) {
        System.out.println(text);
    }

    public static String eingabe() {
        return scanner.nextLine();
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

    public static boolean eingabeBoolean() {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("ja")) return true;
            if (input.equals("false") || input.equals("nein")) return false;
            ausgabe("Bitte 'true'/'false' oder 'ja'/'nein' eingeben: ");
        }
    }

    public static void pauseBisEnter() {
        System.out.print("\n🔁 Drücken Sie ENTER, um fortzufahren...");
        scanner.nextLine();
    }
}
