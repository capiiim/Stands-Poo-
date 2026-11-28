package filey.pd6.bd;

import filey.pd6.modelo.Pago;
import filey.pd6.excepciones.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {
    private final ConexionBD bd;
    public PagoDAO(ConexionBD bd) { this.bd = bd; }

    public void crear(Pago dato) throws SQLException, RegistroNoEncontradoException {
        guardar(dato, false);
    }

    public void actualizar(Pago dato) throws SQLException, RegistroNoEncontradoException {
        guardar(dato, true);
    }

    private void guardar(Pago dato, boolean actualizar) throws SQLException, RegistroNoEncontradoException {
        if (dato == null) throw new DatosInvalidosException("Pago obligatorio.");
        try (Connection c = bd.conectar()) {
            c.setAutoCommit(false);
            try {
                if (actualizar && buscar(c, dato.id()).reservacionId() != dato.reservacionId())
                    throw new PagoInvalidoException(dato.reservacionId(), "No se puede cambiar la reservacion de un pago.");
                validarPago(c, dato, actualizar ? dato.id() : 0);
                String sql = actualizar
                        ? "UPDATE pagos SET fecha_pago = ?, monto = ? WHERE id = ?"
                        : "INSERT INTO pagos (fecha_pago, monto, id, reservacion_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, dato.fechaPago().toString());
                    ps.setString(2, dato.monto().toPlainString());
                    ps.setInt(3, dato.id());
                    if (!actualizar) ps.setInt(4, dato.reservacionId());
                    if (ps.executeUpdate() == 0) throw new RegistroNoEncontradoException("Pago", dato.id());
                }
                c.commit();
            } catch (SQLException | RegistroNoEncontradoException | RuntimeException ex) {
                try { c.rollback(); } catch (SQLException cierre) { ex.addSuppressed(cierre); }
                throw ex;
            }
        }
    }

    private void validarPago(Connection c, Pago dato, int excluirId) throws SQLException, RegistroNoEncontradoException {
        int r = dato.reservacionId();
        BigDecimal total;
        try (PreparedStatement ps = c.prepareStatement("SELECT fecha_reserva, fecha_limite, estado, costo_total FROM reservaciones WHERE id = ?")) {
            ps.setInt(1, r);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new RegistroNoEncontradoException("Reservacion", r);
                if (!"ACTIVA".equals(rs.getString("estado")))
                    throw new PagoInvalidoException(r, "La reservacion no esta activa.");
                if (dato.fechaPago().isBefore(LocalDate.parse(rs.getString("fecha_reserva"))) ||
                    dato.fechaPago().isAfter(LocalDate.parse(rs.getString("fecha_limite"))))
                    throw new PagoInvalidoException(r, "La fecha del pago esta fuera del plazo.");
                total = new BigDecimal(rs.getString("costo_total"));
            }
        }
        if (sumar(c, r, excluirId).add(dato.monto()).compareTo(total) > 0)
            throw new PagoInvalidoException(r, "El monto supera el saldo pendiente.");
    }

    private BigDecimal sumar(Connection c, int reservacionId, int excluirId) throws SQLException {
        BigDecimal total = new BigDecimal("0.00");
        try (PreparedStatement ps = c.prepareStatement("SELECT monto FROM pagos WHERE reservacion_id = ? AND id <> ?")) {
            ps.setInt(1, reservacionId);
            ps.setInt(2, excluirId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) total = total.add(new BigDecimal(rs.getString("monto")));
            }
        }
        return total;
    }

    public BigDecimal saldoPendiente(int reservacionId) throws SQLException, RegistroNoEncontradoException {
        validarId(reservacionId);
        try (Connection c = bd.conectar()) {
            c.setAutoCommit(false);
            BigDecimal total;
            try (PreparedStatement ps = c.prepareStatement("SELECT costo_total FROM reservaciones WHERE id = ?")) {
                ps.setInt(1, reservacionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new RegistroNoEncontradoException("Reservacion", reservacionId);
                    total = new BigDecimal(rs.getString("costo_total"));
                }
            }
            BigDecimal saldo = total.subtract(sumar(c, reservacionId, 0));
            c.commit();
            return saldo;
        }
    }

    public List<Pago> listar() throws SQLException {
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement("SELECT * FROM pagos ORDER BY fecha_pago, id")) {
            return leerLista(ps);
        }
    }

    public List<Pago> listarPorMes(YearMonth mes) throws SQLException {
        if (mes == null) throw new DatosInvalidosException("Mes obligatorio.");
        String sql = "SELECT * FROM pagos WHERE fecha_pago >= ? AND fecha_pago < ? ORDER BY fecha_pago, id";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, mes.atDay(1).toString());
            ps.setString(2, mes.plusMonths(1).atDay(1).toString());
            return leerLista(ps);
        }
    }

    public List<Pago> listarPorReservacion(int editorialId, int reservacionId) throws SQLException, RegistroNoEncontradoException {
        validarId(editorialId);
        validarId(reservacionId);
        String sql = "SELECT p.* FROM pagos p JOIN reservaciones r ON p.reservacion_id = r.id WHERE r.editorial_id = ? AND r.id = ? ORDER BY p.fecha_pago, p.id";
        try (Connection c = bd.conectar()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement("SELECT id FROM reservaciones WHERE id = ? AND editorial_id = ?")) {
                ps.setInt(1, reservacionId);
                ps.setInt(2, editorialId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new RegistroNoEncontradoException("Reservacion de la editorial " + editorialId, reservacionId);
                }
            }
            List<Pago> datos;
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, editorialId);
                ps.setInt(2, reservacionId);
                datos = leerLista(ps);
            }
            c.commit();
            return datos;
        }
    }

    public Pago buscar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        try (Connection c = bd.conectar()) { return buscar(c, id); }
    }

    private Pago buscar(Connection c, int id) throws SQLException, RegistroNoEncontradoException {
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM pagos WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return leer(rs);
            }
        }
        throw new RegistroNoEncontradoException("Pago", id);
    }

    public void eliminar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement("DELETE FROM pagos WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new RegistroNoEncontradoException("Pago", id);
        }
    }

    private void validarId(int id) {
        if (id <= 0) throw new DatosInvalidosException("El ID debe ser positivo.");
    }

    private List<Pago> leerLista(PreparedStatement ps) throws SQLException {
        List<Pago> datos = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) datos.add(leer(rs));
        }
        return datos;
    }

    private Pago leer(ResultSet rs) throws SQLException {
        return new Pago(rs.getInt("id"), rs.getInt("reservacion_id"), LocalDate.parse(rs.getString("fecha_pago")), new BigDecimal(rs.getString("monto")));
    }
}
