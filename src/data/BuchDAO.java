package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Nutzer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import exception.SQLAbfrageFehlgeschlagenException;
import model.Buch;

/**
 * Die Klasse enthält alle nötigen SQL Operationen, um an
 * der Tabelle Buch die nötigen Operationen auszuführen.
 */
@SuppressWarnings("java:S6548")
public class BuchDAO implements GenericDAO<Buch> {

    private static final BuchDAO INSTANCE = new BuchDAO();
    private static final String BUCH_NICHT_NULL = "Buch darf nicht null sein.";

    public static BuchDAO getInstance() {
        return INSTANCE;
    }

    private BuchDAO() {}

    @Override
    public void save(@NotNull Buch buch) {
        Objects.requireNonNull(buch, BUCH_NICHT_NULL);
        try (Connection con = DBConnect.getConnection()) {
            String sql = "INSERT INTO Buch (titel, author, bookId, available, rentingStatus, nutzerId, fernleihe) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) { // Bietet mehr Sicherheit, leichter zu verstehen
                stmt.setString(1, buch.getTitel());
                stmt.setString(2, buch.getAuthor());
                stmt.setInt(3, buch.getBookId());
                stmt.setBoolean(4, buch.isAvailable());
                stmt.setBoolean(5, buch.isRentingStatus());
                stmt.setInt(6, buch.getAusgeliehenAnNutzer().getCustomerId());
                stmt.setBoolean(7, buch.isFernleihe());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    @Nullable
    public Buch findById(int id) {
        try (Connection con = DBConnect.getConnection()) {
            String sql = "SELECT * FROM Buch WHERE bookId = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return extractBuch(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }

    @Override
    public void update(@NotNull Buch buch) {
        Objects.requireNonNull(buch, BUCH_NICHT_NULL);
        String sql = "UPDATE Buch SET titel = ?, author = ?, available = ?, rentingStatus = ?, nutzerId = ?, fernleihe = ? WHERE bookId = ?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, buch.getTitel());
            stmt.setString(2, buch.getAuthor());
            stmt.setBoolean(3, buch.isAvailable());
            stmt.setBoolean(4, buch.isRentingStatus());
            if (buch.getAusgeliehenAnNutzer() != null) {
                stmt.setInt(5, buch.getAusgeliehenAnNutzer().getCustomerId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.setInt(6, buch.getBookId());
            stmt.setBoolean(7, buch.isFernleihe());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public void delete(@NotNull Buch buch) {
        Objects.requireNonNull(buch, BUCH_NICHT_NULL);
        String sql = "DELETE FROM Buch WHERE bookId = ?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, buch.getBookId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public List<Buch> getAll() {
        List<Buch> buecherListe = new ArrayList<>();
        String sql = "SELECT * FROM Buch";
        try (Connection con = DBConnect.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                buecherListe.add(extractBuch(rs));
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return buecherListe;
    }

    private Buch extractBuch(ResultSet rs) throws SQLException {
        int id = rs.getInt("bookId");
        String titel = rs.getString("titel");
        String author = rs.getString("author");
        boolean available = rs.getBoolean("available");
        boolean rentingStatus = rs.getBoolean("rentingStatus");
        Nutzer nutzer = NutzerDAO.getInstance().findById(rs.getInt("nutzerId"));
        boolean fernleihe = rs.getBoolean("fernleihe");
        return new Buch(titel, author, id, available, rentingStatus, nutzer, fernleihe);
    }
}
