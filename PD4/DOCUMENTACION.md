# Documentación de lo implementado

**Objetivo:** demostrar lecturas de stands, editoriales y reservaciones desde una base de datos mediante JDBC, con separación MVC + capa de BD.

**Flujo:** `Main` → `ConsultaController` → DAO → `ConexionBD` → SQLite → objetos Modelo → `ConsolaVista`.

**Modelo:** entidades inmutables mediante `record`. **Vista:** presentación de listas por consola. **Controlador:** coordinación y captura de errores SQL. **BD:** conexión con `DriverManager`, inicialización de tablas y tres consultas SELECT.

**Relaciones:** cada reservación referencia un stand y una editorial por sus IDs; SQLite verifica claves foráneas en cada conexión.

**Decisiones:** SQLite evita depender de un servidor externo; Maven gestiona el driver JDBC; `BigDecimal` evita imprecisiones de punto flotante; `PreparedStatement` permite parametrizar consultas si se amplía el programa. Los importes se guardan como texto decimal para conservar su representación exacta.

**Prueba manual:** ejecutar `mvn compile exec:java`; debe verse `Conexion JDBC a SQLite correcta.` y tres encabezados con registros. Repetir la ejecución no debe duplicar filas porque los datos de prueba usan `INSERT OR IGNORE`.

**Límites:** el módulo es una demostración de lectura. No implementa altas, modificaciones o bajas vía JDBC ni migra el backend previo a SQLite.
