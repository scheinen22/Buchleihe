package server.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import server.exception.SQLAbfrageFehlgeschlagenException;
import client.model.Nutzer;

@SuppressWarnings("java:S6548")
public class NutzerDAO implements GenericDAO<Nutzer> {

    private static final NutzerDAO INSTANCE = new NutzerDAO();
    private static final String NUTZER_NICHT_NULL = "Nutzer darf nicht null sein";

    public static NutzerDAO getInstance() {
        return INSTANCE;
    }

    private NutzerDAO() {}

    @Override
    public void save(@NotNull Nutzer nutzer) {
        Objects.requireNonNull(nutzer, NUTZER_NICHT_NULL);
        String sql = "INSERT INTO Nutzer (name, surname, id, benutzername, passwort, mitarbeiterstatus) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nutzer.getName());
            stmt.setString(2, nutzer.getSurname());
            stmt.setInt(3, nutzer.getCustomerId());
            stmt.setString(4, nutzer.getBenutzername());
            stmt.setString(5, nutzer.getPasswort());
            stmt.setBoolean(6, nutzer.isMitarbeiter());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    @Nullable
    public Nutzer findById(int id) {
        String sql = "SELECT * FROM Nutzer WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractNutzer(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }

    @Nullable
    public Nutzer findByBenutzernameUndPasswort(String benutzername, String passwort) {
        String sql = "SELECT * FROM Nutzer WHERE benutzername = ? AND passwort = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, benutzername);
            stmt.setString(2, passwort);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractNutzer(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }

    @Override
    public void update(@NotNull Nutzer nutzer) { // Evtl. Mitarbeiter Logik einbauen?
        Objects.requireNonNull(nutzer, NUTZER_NICHT_NULL);
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
    public void delete(@NotNull Nutzer nutzer) { // Evtl. Mitarbeiter Logik einbauen?
        Objects.requireNonNull(nutzer, NUTZER_NICHT_NULL);
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
    public List<Nutzer> getAll() {
        List<Nutzer> nutzerListe = new ArrayList<>();
        String sql = "SELECT * FROM Nutzer";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                nutzerListe.add(extractNutzer(rs));
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return nutzerListe;
    }

    /**
     * Extrahiert die Daten aus dem ResultSet und erstellt ein Nutzer-Objekt.
     * @param rs Das ResultSet aus der Datenbankabfrage.
     * @return Ein Nutzer-Objekt.
     * @throws SQLException Wenn ein Fehler beim Zugriff auf das ResultSet auftritt.
     */
    private Nutzer extractNutzer(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String benutzername = rs.getString("benutzername");
        String passwort = rs.getString("passwort");
        boolean mitarbeiterstatus = rs.getBoolean("mitarbeiterstatus");
        return new Nutzer(name, surname, id, benutzername, passwort, mitarbeiterstatus);
    }
}
