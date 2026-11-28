package filey.pd5.vista;
import filey.pd5.modelo.*;import java.util.List;
public class ConsolaVista {
 public void mensaje(String s){System.out.println(s);}
 public void error(String s){System.err.println(s);}
 public void mostrarStands(List<Stand> xs){System.out.println("\n=== STANDS ===");for(Stand x:xs)System.out.printf("ID %d | %s m2 | $%s/m2 | Total $%s | %s%n",x.id(),x.metrosCuadrados(),x.precioPorMetro(),x.precioTotal(),x.estado());}
 public void mostrarEditoriales(List<Editorial> xs){System.out.println("\n=== EDITORIALES ===");for(Editorial x:xs)System.out.printf("ID %d | %s | %s%n",x.id(),x.nombre(),x.correo());}
 public void mostrarReservaciones(List<Reservacion> xs){System.out.println("\n=== RESERVACIONES ===");for(Reservacion x:xs)System.out.printf("ID %d | Stand %d | Editorial %d | %s al %s | %s%n",x.id(),x.standId(),x.editorialId(),x.fechaReserva(),x.fechaLimite(),x.estado());}
}
