package filey.pd4.bd;
import filey.pd4.modelo.Editorial;import java.sql.*;import java.util.*;
public class EditorialDAO {
 private final ConexionBD bd;
 public EditorialDAO(ConexionBD bd){this.bd=bd;}
 public List<Editorial> listar() throws SQLException {
  List<Editorial> datos=new ArrayList<>();
  try(Connection c=bd.conectar();PreparedStatement ps=c.prepareStatement("SELECT id,nombre,correo FROM editoriales ORDER BY id");ResultSet rs=ps.executeQuery()) {
   while(rs.next()) datos.add(new Editorial(rs.getInt("id"),rs.getString("nombre"),rs.getString("correo")));
  }return datos;
 }
}
