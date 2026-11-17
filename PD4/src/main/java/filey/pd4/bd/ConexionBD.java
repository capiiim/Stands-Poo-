package filey.pd4.bd;
import java.sql.*;
public class ConexionBD {
 private static final String URL = "jdbc:sqlite:filey_pd4.db";
 public Connection conectar() throws SQLException {
  Connection c = DriverManager.getConnection(URL);
  try (Statement s = c.createStatement()) { s.execute("PRAGMA foreign_keys = ON"); }
  return c;
 }
 public void inicializar() throws SQLException {
  try (Connection c = conectar(); Statement s = c.createStatement()) {
   s.executeUpdate("CREATE TABLE IF NOT EXISTS stands (id INTEGER PRIMARY KEY, metros_cuadrados TEXT NOT NULL, precio_por_metro TEXT NOT NULL, estado TEXT NOT NULL)");
   s.executeUpdate("CREATE TABLE IF NOT EXISTS editoriales (id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, correo TEXT NOT NULL)");
   s.executeUpdate("CREATE TABLE IF NOT EXISTS reservaciones (id INTEGER PRIMARY KEY, stand_id INTEGER NOT NULL REFERENCES stands(id), editorial_id INTEGER NOT NULL REFERENCES editoriales(id), fecha_reserva TEXT NOT NULL, fecha_limite TEXT NOT NULL, estado TEXT NOT NULL)");
   s.executeUpdate("INSERT OR IGNORE INTO stands VALUES (1,'10.00','2000.00','RESERVADO'),(2,'12.00','1500.00','DISPONIBLE'),(3,'8.00','1800.00','DISPONIBLE')");
   s.executeUpdate("INSERT OR IGNORE INTO editoriales VALUES (1,'Editorial Demo','editorial@ejemplo.test'),(2,'Libros del Sur','contacto@ejemplo.test')");
   s.executeUpdate("INSERT OR IGNORE INTO reservaciones VALUES (1,1,1,'2026-11-12','2026-11-19','ACTIVA')");
  }
 }
}
