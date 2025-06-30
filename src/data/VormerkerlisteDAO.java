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

@SuppressWarnings("java:S6548")
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
                    return extractVormerkerliste(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }

    public boolean istSchonVorgemerkt(int buchId, int nutzerId) {
        String sql = "SELECT COUNT(*) FROM Vormerkerliste WHERE buchId = ? AND nutzerId = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, buchId);
            stmt.setInt(2, nutzerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return false;
    }

    public List<Vormerkerliste> findByBookIdSorted(int buchId) {
        List<Vormerkerliste> liste = new ArrayList<>();
        String sql = "SELECT * FROM Vormerkerliste WHERE buchId = ? ORDER BY eintrittsDatum ASC";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, buchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    liste.add(extractVormerkerliste(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return liste;
    }

    @Override
    public void update(@NotNull Vormerkerliste vormerkerliste) {
        Objects.requireNonNull(vormerkerliste, VORMERKERLISTE_NICHT_NULL);
        String sql = "UPDATE Vormerkerliste SET eintrittsDatum = ? WHERE nutzerId = ? AND buchId = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, vormerkerliste.getEintrittsDatum());
            stmt.setInt(2, vormerkerliste.getNutzer().getCustomerId());
            stmt.setInt(3, vormerkerliste.getBuch().getBookId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public void delete(@NotNull Vormerkerliste vormerkerliste) {
        Objects.requireNonNull(vormerkerliste, VORMERKERLISTE_NICHT_NULL);
        String sql = "DELETE FROM Vormerkerliste WHERE nutzerId = ? AND buchId = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, vormerkerliste.getNutzer().getCustomerId());
            stmt.setInt(2, vormerkerliste.getBuch().getBookId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public List<Vormerkerliste> getAll() {
        List<Vormerkerliste> vormerkerlisteListe = new ArrayList<>();
        String sql = "SELECT * FROM Vormerkerliste";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vormerkerlisteListe.add(extractVormerkerliste(rs));
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return vormerkerlisteListe;
    }

    /**
     * Extrahiert die Daten aus dem ResultSet und erstellt ein Vormerkerliste-Objekt.
     * @param rs Das ResultSet aus der Datenbankabfrage.
     * @return Ein Vormerkerliste-Objekt.
     * @throws SQLException Wenn ein Fehler beim Zugriff auf das ResultSet auftritt.
     */
    private Vormerkerliste extractVormerkerliste(ResultSet rs) throws SQLException {
        Nutzer nutzer = NutzerDAO.getInstance().findById(rs.getInt("nutzerId"));
        Buch buch = BuchDAO.getInstance().findById(rs.getInt("buchId"));
        return new Vormerkerliste(nutzer, buch, rs.getDate("eintrittsDatum"));
    }
}
