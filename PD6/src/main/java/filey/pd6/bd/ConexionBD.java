package filey.pd6.bd;

import java.sql.*;

public class ConexionBD {
    private static final String URL = "jdbc:sqlite:filey_pd6.db";

    public Connection conectar() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        try (Statement s = c.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            try { c.close(); } catch (SQLException cierre) { e.addSuppressed(cierre); }
            throw e;
        }
        return c;
    }

    public void inicializar() throws SQLException {
        try (Connection c = conectar(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS stands (id INTEGER PRIMARY KEY, metros_cuadrados TEXT NOT NULL, precio_por_metro TEXT NOT NULL, estado TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS editoriales (id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, correo TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS reservaciones (id INTEGER PRIMARY KEY, stand_id INTEGER NOT NULL REFERENCES stands(id), editorial_id INTEGER NOT NULL REFERENCES editoriales(id), fecha_reserva TEXT NOT NULL, fecha_limite TEXT NOT NULL, estado TEXT NOT NULL, costo_total TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS pagos (id INTEGER PRIMARY KEY, reservacion_id INTEGER NOT NULL REFERENCES reservaciones(id) ON DELETE RESTRICT, fecha_pago TEXT NOT NULL, monto TEXT NOT NULL)");
            s.executeUpdate("CREATE INDEX IF NOT EXISTS idx_pagos_fecha ON pagos(fecha_pago)");
            s.executeUpdate("CREATE INDEX IF NOT EXISTS idx_pagos_reservacion ON pagos(reservacion_id)");
        }
    }
}
