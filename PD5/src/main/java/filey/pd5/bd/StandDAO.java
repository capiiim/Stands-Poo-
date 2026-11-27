package filey.pd5.bd;

import filey.pd5.modelo.Stand;
import filey.pd5.excepciones.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class StandDAO {
    private final ConexionBD bd;
    public StandDAO(ConexionBD bd) { this.bd = bd; }

    public void crear(Stand dato) throws SQLException {
        if (dato == null) throw new DatosInvalidosException("Registro obligatorio.");
        String sql = "INSERT INTO stands (id, metros_cuadrados, precio_por_metro, estado) VALUES (?, ?, ?, ?)";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, dato.id());
            ps.setString(2, dato.metrosCuadrados().toPlainString());
            ps.setString(3, dato.precioPorMetro().toPlainString());
            ps.setString(4, dato.estado());
            ps.executeUpdate();
        }
    }

    public List<Stand> listar() throws SQLException {
        List<Stand> datos = new ArrayList<>();
        String sql = "SELECT id, metros_cuadrados, precio_por_metro, estado FROM stands ORDER BY id";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) datos.add(leer(rs));
        }
        return datos;
    }

    public Stand buscar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        String sql = "SELECT id, metros_cuadrados, precio_por_metro, estado FROM stands WHERE id = ?";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return leer(rs);
            }
        }
        throw new RegistroNoEncontradoException("Stand", id);
    }

    public void actualizar(Stand dato) throws SQLException, RegistroNoEncontradoException {
        if (dato == null) throw new DatosInvalidosException("Registro obligatorio.");
        String sql = "UPDATE stands SET metros_cuadrados = ?, precio_por_metro = ?, estado = ? WHERE id = ?";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dato.metrosCuadrados().toPlainString());
            ps.setString(2, dato.precioPorMetro().toPlainString());
            ps.setString(3, dato.estado());
            ps.setInt(4, dato.id());
            if (ps.executeUpdate() == 0) throw new RegistroNoEncontradoException("Stand", dato.id());
        }
    }

    public void eliminar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement("DELETE FROM stands WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new RegistroNoEncontradoException("Stand", id);
        }
    }

    private void validarId(int id) {
        if (id <= 0) throw new DatosInvalidosException("El ID debe ser positivo.");
    }

    private Stand leer(ResultSet rs) throws SQLException {
        return new Stand(rs.getInt("id"), new BigDecimal(rs.getString("metros_cuadrados")), new BigDecimal(rs.getString("precio_por_metro")), rs.getString("estado"));
    }
}
