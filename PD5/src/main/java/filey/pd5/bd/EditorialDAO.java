package filey.pd5.bd;

import filey.pd5.modelo.Editorial;
import filey.pd5.excepciones.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EditorialDAO {
    private final ConexionBD bd;
    public EditorialDAO(ConexionBD bd) { this.bd = bd; }

    public void crear(Editorial dato) throws SQLException {
        if (dato == null) throw new DatosInvalidosException("Registro obligatorio.");
        String sql = "INSERT INTO editoriales (id, nombre, correo) VALUES (?, ?, ?)";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, dato.id());
            ps.setString(2, dato.nombre());
            ps.setString(3, dato.correo());
            ps.executeUpdate();
        }
    }

    public List<Editorial> listar() throws SQLException {
        List<Editorial> datos = new ArrayList<>();
        String sql = "SELECT id, nombre, correo FROM editoriales ORDER BY id";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) datos.add(leer(rs));
        }
        return datos;
    }

    public Editorial buscar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        String sql = "SELECT id, nombre, correo FROM editoriales WHERE id = ?";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return leer(rs);
            }
        }
        throw new RegistroNoEncontradoException("Editorial", id);
    }

    public void actualizar(Editorial dato) throws SQLException, RegistroNoEncontradoException {
        if (dato == null) throw new DatosInvalidosException("Registro obligatorio.");
        String sql = "UPDATE editoriales SET nombre = ?, correo = ? WHERE id = ?";
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dato.nombre());
            ps.setString(2, dato.correo());
            ps.setInt(3, dato.id());
            if (ps.executeUpdate() == 0) throw new RegistroNoEncontradoException("Editorial", dato.id());
        }
    }

    public void eliminar(int id) throws SQLException, RegistroNoEncontradoException {
        validarId(id);
        try (Connection c = bd.conectar(); PreparedStatement ps = c.prepareStatement("DELETE FROM editoriales WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) throw new RegistroNoEncontradoException("Editorial", id);
        }
    }

    private void validarId(int id) {
        if (id <= 0) throw new DatosInvalidosException("El ID debe ser positivo.");
    }

    private Editorial leer(ResultSet rs) throws SQLException {
        return new Editorial(rs.getInt("id"), rs.getString("nombre"), rs.getString("correo"));
    }
}
