package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import exception.SQLAbfrageFehlgeschlagenException;
import model.Nutzer;
import model.Vorschlag;
import model.VorschlagsStatus;

@SuppressWarnings("java:S6548")
public class VorschlagDAO implements GenericDAO<Vorschlag> {

    private static final VorschlagDAO INSTANCE = new VorschlagDAO();
    private static final String VORSCHLAG_NICHT_NULL = "Vorschlag darf nicht null sein";

    private VorschlagDAO() {}

    public static VorschlagDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(@NotNull Vorschlag v) {
        Objects.requireNonNull(v, VORSCHLAG_NICHT_NULL);
        String sql = "INSERT INTO Vorschlag (buchTitel, autor, status, nutzerId, benachrichtigt) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, v.getBuchTitel());
            stmt.setString(2, v.getAutor());
            stmt.setString(3, v.getStatus().name());
            stmt.setInt(4, v.getNutzer().getCustomerId());
            stmt.setBoolean(5, v.isBenachrichtigt());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public @Nullable Vorschlag findById(int id) {
        String sql = "SELECT * FROM Vorschlag WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return extract(rs);
                }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }

    @Override
    public void update(@NotNull Vorschlag v) {
        Objects.requireNonNull(v, VORSCHLAG_NICHT_NULL);
        String sql = "UPDATE Vorschlag SET buchTitel = ?, autor = ?, status = ?, nutzerId = ?, benachrichtigt = ? WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, v.getBuchTitel());
            stmt.setString(2, v.getAutor());
            stmt.setString(3, v.getStatus().name());
            stmt.setInt(4, v.getNutzer().getCustomerId());
            stmt.setBoolean(5, v.isBenachrichtigt());
            stmt.setInt(6, v.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public void delete(@NotNull Vorschlag v) {
        Objects.requireNonNull(v, VORSCHLAG_NICHT_NULL);
        String sql = "DELETE FROM Vorschlag WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, v.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public List<Vorschlag> getAll() {
        String sql = "SELECT * FROM Vorschlag";
        List<Vorschlag> list = new ArrayList<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return list;
    }

    public List<Vorschlag> findNichtBenachrichtigte(int id) {
        String sql = "SELECT * FROM Vorschlag WHERE benachrichtigt = FALSE AND nutzerId = ?";
        List<Vorschlag> list = new ArrayList<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return list;
    }

    /**
     * Extrahiert die Daten aus dem ResultSet und erstellt ein Vorschlag-Objekt.
     * @param rs Das ResultSet aus der Datenbankabfrage.
     * @return Ein Vorschlag-Objekt.
     * @throws SQLException Wenn ein Fehler beim Zugriff auf das ResultSet auftritt.
     */
    private Vorschlag extract(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String titel = rs.getString("buchTitel");
        String autor = rs.getString("autor");
        VorschlagsStatus status = VorschlagsStatus.valueOf(rs.getString("status"));
        boolean benachrichtigt = rs.getBoolean("benachrichtigt");
        int nutzerId = rs.getInt("nutzerId");
        Nutzer nutzer = NutzerDAO.getInstance().findById(nutzerId);
        return new Vorschlag(id, titel, autor, status, nutzer, benachrichtigt);
    }
}
