package filey.pd4.bd;
import filey.pd4.modelo.Stand;
import java.sql.*;import java.util.*;import java.math.BigDecimal;
public class StandDAO {
 private final ConexionBD bd;
 public StandDAO(ConexionBD bd){this.bd=bd;}
 public List<Stand> listar() throws SQLException {
  List<Stand> datos=new ArrayList<>();
  try(Connection c=bd.conectar();PreparedStatement ps=c.prepareStatement("SELECT id, metros_cuadrados, precio_por_metro, estado FROM stands ORDER BY id");ResultSet rs=ps.executeQuery()) {
   while(rs.next()) datos.add(new Stand(rs.getInt("id"),new BigDecimal(rs.getString("metros_cuadrados")),new BigDecimal(rs.getString("precio_por_metro")),rs.getString("estado")));
  } return datos;
 }
}
