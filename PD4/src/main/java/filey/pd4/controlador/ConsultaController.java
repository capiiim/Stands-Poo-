package filey.pd4.controlador;
import filey.pd4.bd.*;import filey.pd4.vista.ConsolaVista;import java.sql.SQLException;
public class ConsultaController {
 private final ConexionBD bd;private final ConsolaVista vista;
 public ConsultaController(ConexionBD bd, ConsolaVista vista){this.bd=bd;this.vista=vista;}
 public void ejecutar(){
  try {
   bd.inicializar();vista.mensaje("Conexion JDBC a SQLite correcta.");
   vista.mostrarStands(new StandDAO(bd).listar());
   vista.mostrarEditoriales(new EditorialDAO(bd).listar());
   vista.mostrarReservaciones(new ReservacionDAO(bd).listar());
  } catch(SQLException e){vista.error("No se pudo consultar la BD: "+e.getMessage());}
 }
}
