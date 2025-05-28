package controller;

import view.View;

public class Controller {
    public static void main(String[] args) {
        View.eingabe();
        View.eingabeInt();
        if (View.eingabeBoolean()) {
            View.ausgabe("True wurde ausgegeben");
        } else {
            View.ausgabe("False wurde ausgegeben");
        }

    }
}
