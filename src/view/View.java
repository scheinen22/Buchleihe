package view;

import java.util.Scanner;

public class View {
    public static void ausgabe(String text) {
        System.out.println(text);
    }
    public static String eingabe() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    public static int eingabeInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
    public static boolean eingabeBoolean() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextBoolean();
    }
    private View() {
        throw new IllegalStateException("Utility class");
    }
}
