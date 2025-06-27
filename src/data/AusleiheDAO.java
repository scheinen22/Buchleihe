package data;

import exception.SQLAbfrageFehlgeschlagenException;
import model.Ausleihe;
import model.Buch;
import model.Nutzer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AusleiheDAO implements GenericDAO<Ausleihe> {

    private static final AusleiheDAO INSTANCE = new AusleiheDAO();
    private static final String AUSLEIHE_NICHT_NULL = "Ausleihe darf nicht null sein.";

    private AusleiheDAO() {}

    public static AusleiheDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(@NotNull Ausleihe a) {
        Objects.requireNonNull(a, AUSLEIHE_NICHT_NULL);
        String sql = "INSERT INTO Ausleihe (buch_Id, nutzer_Id, ausleihdatum, rueckgabedatum, fernleihe) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, a.getBuch().getBookId());
            stmt.setInt(2, a.getNutzer().getCustomerId());
            stmt.setDate(3, a.getAusleihdatum());
            stmt.setDate(4, a.getRueckgabedatum());
            stmt.setBoolean(5, a.isFernleihe());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    @Nullable
    public Ausleihe findById(int id) {
        String sql = "SELECT * FROM Ausleihe WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extract(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return null;
    }

    @Override
    public void update(@NotNull Ausleihe a) {
        Objects.requireNonNull(a, AUSLEIHE_NICHT_NULL);
        String sql = "UPDATE Ausleihe SET buch_Id = ?, nutzer_Id = ?, ausleihdatum = ?, rueckgabedatum = ?, fernleihe = ? WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, a.getBuch().getBookId());
            stmt.setInt(2, a.getNutzer().getCustomerId());
            stmt.setDate(3, a.getAusleihdatum());
            stmt.setDate(4, a.getRueckgabedatum());
            stmt.setBoolean(5, a.isFernleihe());
            stmt.setInt(6, a.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public void delete(@NotNull Ausleihe a) {
        Objects.requireNonNull(a, AUSLEIHE_NICHT_NULL);
        String sql = "DELETE FROM Ausleihe WHERE id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, a.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
    }

    @Override
    public List<Ausleihe> getAll() {
        String sql = "SELECT * FROM Ausleihe";
        List<Ausleihe> list = new ArrayList<>();
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

    @Nullable
    public List<Ausleihe> findAlleBuecherByBuchIdUndOffen(int buchId) {
        String sql = "SELECT * FROM Ausleihe WHERE buch_Id = ? AND rueckgabedatum IS NULL";
        List<Ausleihe> list = new ArrayList<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, buchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extract(rs));
                }
            }
        } catch (SQLException e) {
            throw new SQLAbfrageFehlgeschlagenException(e);
        }
        return list;
    }

    private Ausleihe extract(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int buchId = rs.getInt("buch_Id");
        int nutzerId = rs.getInt("nutzer_Id");
        Date ausleihdatum = rs.getDate("ausleihdatum");
        Date rueckgabedatum = rs.getDate("rueckgabedatum");
        boolean istFernleihe = rs.getBoolean("fernleihe");
        Buch buch = BuchDAO.getInstance().findById(buchId);
        Nutzer nutzer = NutzerDAO.getInstance().findById(nutzerId);
        return new Ausleihe(id, buch, nutzer, ausleihdatum, rueckgabedatum, istFernleihe);
    }
}
