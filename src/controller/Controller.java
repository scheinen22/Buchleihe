package controller;

import data.BuchDAO;
import data.DBConnect;
import model.Buch;
import view.View;

public class Controller {
    public static void main(String[] args) {
        DBConnect db = new DBConnect();
        Buch buch = new Buch("Test", "test", 11, true, false);
        BuchDAO buchDAO = BuchDAO.getInstance();
        buchDAO.save(buch);
    }
}
