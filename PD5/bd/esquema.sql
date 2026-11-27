PRAGMA foreign_keys = ON;
CREATE TABLE IF NOT EXISTS stands (id INTEGER PRIMARY KEY, metros_cuadrados TEXT NOT NULL, precio_por_metro TEXT NOT NULL, estado TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS editoriales (id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, correo TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS reservaciones (id INTEGER PRIMARY KEY, stand_id INTEGER NOT NULL REFERENCES stands(id), editorial_id INTEGER NOT NULL REFERENCES editoriales(id), fecha_reserva TEXT NOT NULL, fecha_limite TEXT NOT NULL, estado TEXT NOT NULL);
