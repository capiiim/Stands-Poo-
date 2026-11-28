package filey.pd5.controlador;

import filey.pd5.bd.*;
import filey.pd5.modelo.*;
import filey.pd5.excepciones.*;
import filey.pd5.vista.ConsolaVista;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CrudController {
    private final ConexionBD bd;
    private final ConsolaVista vista;
    private final StandDAO stands;
    private final EditorialDAO editoriales;
    private final ReservacionDAO reservaciones;

    public CrudController(ConexionBD bd, ConsolaVista vista) {
        this.bd = bd;
        this.vista = vista;
        stands = new StandDAO(bd);
        editoriales = new EditorialDAO(bd);
        reservaciones = new ReservacionDAO(bd);
    }

    public boolean ejecutar() {
        try {
            bd.inicializar();
            vista.mensaje("Conexion JDBC a SQLite correcta.");

            // IDs libres para no modificar registros existentes.
            int s = stands.listar().stream().mapToInt(Stand::id).max().orElse(0) + 1;
            int e = editoriales.listar().stream().mapToInt(Editorial::id).max().orElse(0) + 1;
            int r = reservaciones.listar().stream().mapToInt(Reservacion::id).max().orElse(0) + 1;

            vista.mensaje("\n--- CREATE: altas ---");
            stands.crear(new Stand(s, new BigDecimal("10.00"), new BigDecimal("2000.00"), "DISPONIBLE"));
            editoriales.crear(new Editorial(e, "Editorial Demo", "editorial@ejemplo.test"));
            reservaciones.crear(new Reservacion(r, s, e, "2026-11-12", "2026-11-19", "ACTIVA"));
            stands.actualizar(new Stand(s, new BigDecimal("10.00"), new BigDecimal("2000.00"), "RESERVADO"));
            mostrarTodo();

            vista.mensaje("\n--- READ: buscar por ID ---");
            vista.mostrarStands(List.of(stands.buscar(s)));
            vista.mostrarEditoriales(List.of(editoriales.buscar(e)));
            vista.mostrarReservaciones(List.of(reservaciones.buscar(r)));

            vista.mensaje("\n--- UPDATE: modificaciones ---");
            stands.actualizar(new Stand(s, new BigDecimal("10.00"), new BigDecimal("2200.00"), "RESERVADO"));
            editoriales.actualizar(new Editorial(e, "Editorial Demo Actualizada", "nuevo@ejemplo.test"));
            reservaciones.actualizar(new Reservacion(r, s, e, "2026-11-12", "2026-11-22", "ACTIVA"));
            mostrarTodo();

            vista.mensaje("\n--- DELETE: bajas ---");
            // Primero la reservacion porque contiene las claves foraneas.
            reservaciones.eliminar(r);
            editoriales.eliminar(e);
            stands.eliminar(s);
            mostrarTodo();

            vista.mensaje("\n--- Ejemplos de excepciones ---");
            try {
                stands.buscar(s);
            } catch (RegistroNoEncontradoException ex) {
                vista.mensaje("RegistroNoEncontradoException: " + ex.getMessage());
            }
            try {
                new Editorial(0, "", "correo-invalido");
            } catch (DatosInvalidosException ex) {
                vista.mensaje("DatosInvalidosException: " + ex.getMessage());
            }
            vista.mensaje("Demostracion CRUD terminada.");
            return true;
        } catch (SQLException ex) {
            vista.error("SQLException: " + ex.getMessage());
        } catch (RegistroNoEncontradoException | DatosInvalidosException ex) {
            vista.error(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return false;
    }

    private void mostrarTodo() throws SQLException {
        vista.mostrarStands(stands.listar());
        vista.mostrarEditoriales(editoriales.listar());
        vista.mostrarReservaciones(reservaciones.listar());
    }
}
