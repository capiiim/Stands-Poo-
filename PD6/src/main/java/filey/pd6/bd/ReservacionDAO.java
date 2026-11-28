package filey.pd6.bd;

import filey.pd6.modelo.Reservacion;
import filey.pd6.excepciones.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ReservacionDAO {
    private final ConexionBD bd;
    public ReservacionDAO(ConexionBD bd) { this.bd = bd; }

    public void crear(Reservacion dato) throws SQLException {
        if (dato == null) throw new DatosInvalidosException("Registro obligatorio.");
        String sql = "INSERT INTO reservaciones (id, stand_id, editorial_id, fecha_reserva, fecha_limite, estado, costo_total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, dato.id());
            ps.setInt(2, dato.standId());
            ps.setInt(3, dato.editorialId());
            ps.setString(4, dato.fechaReserva());
            ps.setString(5, dato.fechaLimite());
            ps.setString(6, dato.estado());
            ps.setString(7, dato.costoTotal().toPlainString());
            ps.executeUpdate();
        }
    }

    public List<Reservacion> listar() throws SQLException {
        List<Reservacion> datos = new ArrayList<>();
        String sql = "SELECT id, stand_id, editorial_id, fecha_reserva, fecha_limite, estado, costo_total FROM reservaciones ORDER BY id";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) datos.add(leer(rs));
        }
        return datos;
    }

    public Reservacion buscar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        String sql = "SELECT id, stand_id, editorial_id, fecha_reserva, fecha_limite, estado, costo_total FROM reservaciones WHERE id = ?";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return leer(rs);
            }
        }
        throw new RegistroNoEncontradoException("Reservacion", id);
    }

    public void actualizar(Reservacion dato) throws SQLException, RegistroNoEncontradoException {
        if (dato == null) throw new DatosInvalidosException("Registro obligatorio.");
        String sql = "UPDATE reservaciones SET stand_id = ?, editorial_id = ?, fecha_reserva = ?, fecha_limite = ?, estado = ?, costo_total = ? WHERE id = ? AND (NOT EXISTS (SELECT 1 FROM pagos WHERE reservacion_id = reservaciones.id) OR (stand_id = ? AND editorial_id = ? AND fecha_reserva = ? AND fecha_limite = ? AND costo_total = ?))";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, dato.standId());
            ps.setInt(2, dato.editorialId());
            ps.setString(3, dato.fechaReserva());
            ps.setString(4, dato.fechaLimite());
            ps.setString(5, dato.estado());
            ps.setString(6, dato.costoTotal().toPlainString());
            ps.setInt(7, dato.id());
            ps.setInt(8, dato.standId());
            ps.setInt(9, dato.editorialId());
            ps.setString(10, dato.fechaReserva());
            ps.setString(11, dato.fechaLimite());
            ps.setString(12, dato.costoTotal().toPlainString());
            if (ps.executeUpdate() == 0) {
                buscar(dato.id());
                throw new DatosInvalidosException("No se pueden cambiar stand, editorial, fechas ni costo de una reservacion con pagos.");
            }
        }
    }

    public void eliminar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement("DELETE FROM reservaciones WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new RegistroNoEncontradoException("Reservacion", id);
        }
    }

    private void validarId(int id) {
        if (id <= 0) throw new DatosInvalidosException("El ID debe ser positivo.");
    }

    private Reservacion leer(ResultSet rs) throws SQLException {
        return new Reservacion(rs.getInt("id"), rs.getInt("stand_id"), rs.getInt("editorial_id"), rs.getString("fecha_reserva"), rs.getString("fecha_limite"), rs.getString("estado"), new BigDecimal(rs.getString("costo_total")));
    }
}
