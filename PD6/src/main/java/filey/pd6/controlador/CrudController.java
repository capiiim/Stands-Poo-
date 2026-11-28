package filey.pd6.controlador;

import filey.pd6.bd.*;
import filey.pd6.modelo.*;
import filey.pd6.excepciones.*;
import filey.pd6.vista.ConsolaVista;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;

public class CrudController {
    private final ConexionBD bd;
    private final ConsolaVista vista;
    private final StandDAO stands;
    private final EditorialDAO editoriales;
    private final ReservacionDAO reservaciones;
    private final PagoDAO pagos;

    public CrudController(ConexionBD bd, ConsolaVista vista) {
        this.bd = bd;
        this.vista = vista;
        stands = new StandDAO(bd);
        editoriales = new EditorialDAO(bd);
        reservaciones = new ReservacionDAO(bd);
        pagos = new PagoDAO(bd);
    }

    public boolean ejecutar() {
        try {
            bd.inicializar();
            vista.mensaje("Conexion JDBC a SQLite correcta.");

            // IDs libres para no modificar registros existentes.
            int s = stands.listar().stream().mapToInt(Stand::id).max().orElse(0) + 1;
            int e = editoriales.listar().stream().mapToInt(Editorial::id).max().orElse(0) + 1;
            int r = reservaciones.listar().stream().mapToInt(Reservacion::id).max().orElse(0) + 1;

            int p = pagos.listar().stream().mapToInt(Pago::id).max().orElse(0) + 1;
            LocalDate inicio = LocalDate.of(2026, 11, 12);
            LocalDate limite = inicio.plusDays(20);

            vista.mensaje("\n--- CREATE: altas ---");
            stands.crear(new Stand(s, new BigDecimal("10.00"), new BigDecimal("2000.00"), "DISPONIBLE"));
            editoriales.crear(new Editorial(e, "Editorial Demo", "editorial@ejemplo.test"));
            reservaciones.crear(new Reservacion(r, s, e, inicio.toString(), limite.toString(), "ACTIVA", stands.buscar(s).precioTotal()));
            stands.actualizar(new Stand(s, new BigDecimal("10.00"), new BigDecimal("2000.00"), "RESERVADO"));
            mostrarTodo();

            vista.mensaje("\n--- READ: buscar por ID ---");
            vista.mostrarStands(List.of(stands.buscar(s)));
            vista.mostrarEditoriales(List.of(editoriales.buscar(e)));
            vista.mostrarReservaciones(List.of(reservaciones.buscar(r)));

            vista.mensaje("\n--- UPDATE: modificaciones ---");
            stands.actualizar(new Stand(s, new BigDecimal("10.00"), new BigDecimal("2200.00"), "RESERVADO"));
            editoriales.actualizar(new Editorial(e, "Editorial Demo Actualizada", "nuevo@ejemplo.test"));
            reservaciones.actualizar(new Reservacion(r, s, e, inicio.toString(), limite.plusDays(1).toString(), "ACTIVA", reservaciones.buscar(r).costoTotal()));
            mostrarTodo();

            vista.mensaje("\n--- PAGOS: registrar dos abonos ---");
            pagos.crear(new Pago(p, r, inicio.plusDays(1), new BigDecimal("5000.00")));
            pagos.crear(new Pago(p + 1, r, inicio.plusDays(20), new BigDecimal("10000.00")));
            vista.mostrarPagos(pagos.listar());
            vista.mensaje("Saldo pendiente: $" + pagos.saldoPendiente(r));

            vista.mensaje("\n--- PAGOS: consultar noviembre de 2026 ---");
            vista.mostrarPagos(pagos.listarPorMes(YearMonth.of(2026, 11)));
            vista.mensaje("\n--- PAGOS: consultar diciembre de 2026 ---");
            vista.mostrarPagos(pagos.listarPorMes(YearMonth.of(2026, 12)));
            vista.mensaje("\n--- PAGOS: consultar por editorial y reservacion ---");
            vista.mostrarPagos(pagos.listarPorReservacion(e, r));
            vista.mostrarPagos(List.of(pagos.buscar(p)));

            vista.mensaje("\n--- PAGOS: corregir segundo abono y liquidar ---");
            pagos.actualizar(new Pago(p + 1, r, inicio.plusDays(20), new BigDecimal("15000.00")));
            vista.mostrarPagos(pagos.listarPorReservacion(e, r));
            vista.mensaje("Saldo pendiente: $" + pagos.saldoPendiente(r));
            try {
                pagos.crear(new Pago(p + 2, r, inicio.plusDays(2), new BigDecimal("1.00")));
            } catch (PagoInvalidoException ex) {
                vista.mensaje("PagoInvalidoException: " + ex.getMessage());
            }
            try {
                pagos.crear(new Pago(p + 2, r, limite.plusDays(2), new BigDecimal("1.00")));
            } catch (PagoInvalidoException ex) {
                vista.mensaje("PagoInvalidoException: " + ex.getMessage());
            }

            vista.mensaje("\n--- DELETE: bajas ---");
            // Primero pagos, despues reservacion, editorial y stand.
            pagos.eliminar(p + 1);
            vista.mensaje("Saldo despues de eliminar el segundo abono: $" + pagos.saldoPendiente(r));
            pagos.eliminar(p);
            vista.mostrarPagos(pagos.listar());
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
