package data;

import exception.SQLAbfrageFehlgeschlagenException;
import model.Buch;
import model.Nutzer;
import model.Vormerkerliste;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VormerkerlisteDAO implements GenericDAO<Vormerkerliste> {
    private static final VormerkerlisteDAO INSTANCE = new VormerkerlisteDAO();
    private static final String VORMERKERLISTE_NICHT_NULL = "Vormerkerliste darf nicht null sein";

    private VormerkerlisteDAO() {}

    public static VormerkerlisteDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(@NotNull Vormerkerliste vormerkerliste) {
        Objects.requireNonNull(vormerkerliste, VORMERKERLISTE_NICHT_NULL);
        String sql = "INSERT INTO Vormerkerliste (nutzerId, buchId, eintrittsDatum) VALUES (?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, vormerkerliste.getNutzer().getCustomerId());
            stmt.setInt(2, vormerkerliste.getBuch().getBookId());
            stmt.setDate(3, vormerkerliste.getEintrittsDatum());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public @Nullable Vormerkerliste findById(int id) {
        String sql = "SELECT * FROM Vormerkerliste WHERE nutzerId = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Nutzer nutzer = NutzerDAO.getInstance().findById(rs.getInt("nutzerId"));
                    Buch buch = BuchDAO.getInstance().findById(rs.getInt("buchId"));
                    return new Vormerkerliste(nutzer, buch, rs.getDate("eintrittsDatum"));
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }

    public List<Vormerkerliste> findByBookId(int id) {
        List<Vormerkerliste> vormerkerlisteListe = new ArrayList<>();
        String sql = "SELECT * FROM Vormerkerliste WHERE bookId = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Nutzer nutzer = NutzerDAO.getInstance().findById(rs.getInt("nutzerId"));
                    Buch buch = BuchDAO.getInstance().findById(rs.getInt("buchId"));
                    Vormerkerliste vormerkerliste = new Vormerkerliste(nutzer, buch, rs.getDate("eintrittsDatum"));
                    vormerkerlisteListe.add(vormerkerliste);
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return vormerkerlisteListe;
    }

    @Override
    public void update(@NotNull Vormerkerliste entity) {
        Objects.requireNonNull(nutzer, VORMERKERLISTE_NICHT_NULL);
        String sql = "UPDATE Nutzer SET name = ?, surname = ?, benutzername = ?, passwort = ? WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nutzer.getName());
            stmt.setString(2, nutzer.getSurname());
            stmt.setString(3, nutzer.getBenutzername());
            stmt.setString(4, nutzer.getPasswort());
            stmt.setInt(5, nutzer.getCustomerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public void delete(@NotNull Vormerkerliste entity) {
        Objects.requireNonNull(nutzer, VORMERKERLISTE_NICHT_NULL);
        String sql = "DELETE FROM Nutzer WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, nutzer.getCustomerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public List<Vormerkerliste> getAll() {
        List<Buch> buecherListe = new ArrayList<>();
        String sql = "SELECT * FROM Buch";
        try (Connection con = DBConnect.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titel = rs.getString("titel");
                String author = rs.getString("author");
                boolean available = rs.getBoolean("available");
                boolean rentingStatus = rs.getBoolean("rentingStatus");
                Nutzer nutzer = NutzerDAO.getInstance().findById(rs.getInt("nutzerId"));
                Buch buch = new Buch(titel, author, id, available, rentingStatus, nutzer);
                buecherListe.add(buch);
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return buecherListe;
    }
}
