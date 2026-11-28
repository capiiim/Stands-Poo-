# FILEY — PD4: MVC + Base de Datos + JDBC

Módulo independiente para la PD4, basado en el proyecto FILEY. Conserva la entrega anterior sin reemplazarla.

### Enlace al video

[Ver video explicativo de la PD4](https://drive.google.com/drive/folders/1UF0eufKSEJogPex6UhCE4lgpIoikfzXJ?usp=drive_link)

## Requisitos
- Java JDK 17 o superior
- Maven 3.8 o superior
- Conexión a Internet **la primera vez** para descargar el controlador JDBC de SQLite y dependencias desde Maven Central

## Ejecución
Desde la carpeta `pd4`:

```bash
mvn compile exec:java
```

El programa crea `filey_pd4.db` automáticamente si no existe, crea las tablas y agrega registros de demostración sin duplicarlos. Después muestra la confirmación de conexión JDBC y las listas de **stands, editoriales y reservaciones**.

> No requiere instalar un servidor de base de datos: SQLite guarda los datos en un archivo local. El archivo `.db` se excluye del repositorio; el esquema está en `bd/esquema.sql` y los datos de ejemplo se insertan desde `ConexionBD`.

## Capas MVC + BD
- **Modelo:** `modelo/Stand`, `Editorial`, `Reservacion` representan los registros. `Stand` usa `BigDecimal` para precios.
- **Vista:** `vista/ConsolaVista` imprime resultados sin ejecutar SQL.
- **Controlador:** `controlador/ConsultaController` coordina las lecturas y maneja errores.
- **Base de datos:** `bd/ConexionBD` abre la conexión y prepara tablas; `StandDAO`, `EditorialDAO`, `ReservacionDAO` ejecutan consultas `SELECT` mediante JDBC.
- **Main:** inicia el controlador, que prueba la conexión y muestra las tres tablas.

## Diagramas
- [Diagrama de clases](diagramas/clases.md)
- [Diagrama entidad-relación](diagramas/entidad-relacion.md)


## Alcance
Esta PD4 implementa la **conexión y lectura JDBC** de las tres entidades pedidas, con una vista de consola. No sustituye los servicios de reservas, pagos y notificaciones de la entrega anterior ni afirma integrarlos con SQLite. No incluye escritura de reservas mediante JDBC ni interfaz gráfica porque no se solicitaron para esta actividad.
