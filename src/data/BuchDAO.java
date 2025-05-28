package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import exception.SQLAbfrageFehlgeschlagenException;
import model.Buch;

@SuppressWarnings("java:S6548")
public class BuchDAO implements GenericDAO<Buch> {
    private static final BuchDAO INSTANCE = new BuchDAO();
    private static final String BUCH_NICHT_NULL = "Buch darf nicht null sein.";
    private static final Logger LOGGER = Logger.getLogger(BuchDAO.class.getName());

    public static BuchDAO getInstance() {
        return INSTANCE;
    }

    private BuchDAO() {}

    @Override
    public void save(@NotNull Buch buch) {
        Objects.requireNonNull(buch, BUCH_NICHT_NULL);
        try (Connection con = DBConnect.getConnection()) {
            String sql = "INSERT INTO Buch (titel, author, bookId, available, rentingStatus) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) { // Bietet mehr Sicherheit, leichter zu verstehen
                stmt.setString(1, buch.getTitel());
                stmt.setString(2, buch.getAuthor());
                stmt.setInt(3, buch.getBookId());
                stmt.setBoolean(4, buch.isAvailable());
                stmt.setBoolean(5, buch.isRentingStatus());
                stmt.executeUpdate();
                LOGGER.info("INSERT successful");
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }
    @Override
    @Nullable
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
    public void update(@NotNull Buch buch) {
        Objects.requireNonNull(buch, BUCH_NICHT_NULL);
        String sql = "UPDATE Buch SET titel = ?, author = ?, available = ?, rentingStatus = ? WHERE id = ?";
        try (Connection con = DBConnect.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, buch.getTitel());
            stmt.setString(2, buch.getAuthor());
            stmt.setBoolean(3, buch.isAvailable());
            stmt.setBoolean(4, buch.isRentingStatus());
            stmt.setInt(5, buch.getBookId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }
    @Override
    public void delete(@NotNull Buch buch) {
        Objects.requireNonNull(buch, BUCH_NICHT_NULL);
        String sql = "DELETE FROM Buch WHERE id = ?";
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
                int id = rs.getInt("id");
                String titel = rs.getString("titel");
                String author = rs.getString("author");
                boolean available = rs.getBoolean("available");
                boolean rentingStatus = rs.getBoolean("rentingStatus");
                Buch buch = new Buch(titel, author, id, available, rentingStatus);
                buecherListe.add(buch);
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return buecherListe;
    }
}
