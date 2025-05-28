package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import data.BuchDAO;
import data.DBConnect;
import exception.SQLAbfrageFehlgeschlagenException;

public class BuchDAOImpl implements BuchDAO {
    private DBConnect con;

    public BuchDAOImpl(DBConnect dbConnect) {
        this.con = dbConnect;
    }

    @Override
    public void save(@NotNull Buch buch) {
        try (Connection con = DBConnect.getConnection()) {
            String sql = "INSERT INTO Buch (titel, author, bookId, available, rentingStatus) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) { // Bietet mehr Sicherheit, leichter zu verstehen
                stmt.setString(1, buch.getTitel());
                stmt.setString(2, buch.getAuthor());
                stmt.setInt(3, buch.getBookId());
                stmt.setBoolean(4, buch.isAvailable());
                stmt.setBoolean(5, buch.isRentingStatus());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }
    @Override
    public Buch findById(int id) {
        try (Connection con = DBConnect.getConnection()) {
            String sql = "SELECT * FROM Buch WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int bookId = rs.getInt("id");
                        String titel = rs.getString("titel");
                        String author = rs.getString("author");
                        boolean available = rs.getBoolean("available");
                        boolean rentingStatus = rs.getBoolean("rentingStatus");
                        return new Buch(titel, author, bookId, available, rentingStatus);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }
    @Override
    public void update(Buch buch) {

    }
    @Override
    public void delete(Buch buch) {

    }
    @Override
    public List<Buch> findAll() {
        String sql = "SELECT * FROM Buch";
        return null;
    }
}
