package filey.pd4.bd;
import filey.pd4.modelo.Reservacion;import java.sql.*;import java.util.*;
public class ReservacionDAO {
 private final ConexionBD bd;
 public ReservacionDAO(ConexionBD bd){this.bd=bd;}
 public List<Reservacion> listar() throws SQLException {
  List<Reservacion> datos=new ArrayList<>();
  try(Connection c=bd.conectar();PreparedStatement ps=c.prepareStatement("SELECT id,stand_id,editorial_id,fecha_reserva,fecha_limite,estado FROM reservaciones ORDER BY id");ResultSet rs=ps.executeQuery()) {
   while(rs.next()) datos.add(new Reservacion(rs.getInt("id"),rs.getInt("stand_id"),rs.getInt("editorial_id"),rs.getString("fecha_reserva"),rs.getString("fecha_limite"),rs.getString("estado")));
  }return datos;
 }
}
