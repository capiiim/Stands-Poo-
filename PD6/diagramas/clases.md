# Diagrama de clases PD6

```mermaid
classDiagram
    direction TB
    class Main {
        +main(String[] args) void
    }
    class CrudController {
        -ConexionBD bd
        -ConsolaVista vista
        -StandDAO stands
        -EditorialDAO editoriales
        -ReservacionDAO reservaciones
        -PagoDAO pagos
        +ejecutar() boolean
        -mostrarTodo() void
    }
    class ConsolaVista {
        +mensaje(String s) void
        +error(String s) void
        +mostrarStands(List~Stand~ xs) void
        +mostrarEditoriales(List~Editorial~ xs) void
        +mostrarReservaciones(List~Reservacion~ xs) void
        +mostrarPagos(List~Pago~ xs) void
    }
    class ConexionBD {
        -String URL
        +conectar() Connection
        +inicializar() void
    }
    class StandDAO {
        -ConexionBD bd
        +crear(Stand dato) void
        +listar() List~Stand~
        +buscar(int id) Stand
        +actualizar(Stand dato) void
        +eliminar(int id) void
    }
    class EditorialDAO {
        -ConexionBD bd
        +crear(Editorial dato) void
        +listar() List~Editorial~
        +buscar(int id) Editorial
        +actualizar(Editorial dato) void
        +eliminar(int id) void
    }
    class ReservacionDAO {
        -ConexionBD bd
        +crear(Reservacion dato) void
        +listar() List~Reservacion~
        +buscar(int id) Reservacion
        +actualizar(Reservacion dato) void
        +eliminar(int id) void
    }
    class PagoDAO {
        -ConexionBD bd
        +crear(Pago dato) void
        +listar() List~Pago~
        +buscar(int id) Pago
        +actualizar(Pago dato) void
        +eliminar(int id) void
        +listarPorMes(YearMonth mes) List~Pago~
        +listarPorReservacion(int editorialId, int reservacionId) List~Pago~
        +saldoPendiente(int reservacionId) BigDecimal
        -validarPago(Connection c, Pago dato, int excluirId) void
    }
    class Stand {
        +int id
        +BigDecimal metrosCuadrados
        +BigDecimal precioPorMetro
        +String estado
        +precioTotal() BigDecimal
    }
    class Editorial {
        +int id
        +String nombre
        +String correo
    }
    class Reservacion {
        +int id
        +int standId
        +int editorialId
        +String fechaReserva
        +String fechaLimite
        +String estado
        +BigDecimal costoTotal
    }
    class Pago {
        +int id
        +int reservacionId
        +LocalDate fechaPago
        +BigDecimal monto
    }
    class PagoInvalidoException {
        +PagoInvalidoException(int reservacionId, String motivo)
    }
    class DatosInvalidosException {
        +DatosInvalidosException(String mensaje)
    }
    class RegistroNoEncontradoException {
        +RegistroNoEncontradoException(String entidad, int id)
    }
    class SQLException
    Main --> CrudController
    CrudController --> ConsolaVista
    CrudController --> ConexionBD
    CrudController --> StandDAO
    CrudController --> EditorialDAO
    CrudController --> ReservacionDAO
    CrudController --> PagoDAO
    PagoDAO --> ConexionBD
    PagoDAO ..> Pago
    PagoDAO ..> PagoInvalidoException
    PagoDAO ..> RegistroNoEncontradoException
    PagoDAO ..> DatosInvalidosException
    Pago ..> DatosInvalidosException
    PagoInvalidoException --|> DatosInvalidosException
    ConsolaVista ..> Pago
    Reservacion "1" --> "0..*" Pago : id
    StandDAO --> ConexionBD
    EditorialDAO --> ConexionBD
    ReservacionDAO --> ConexionBD
    StandDAO ..> Stand
    EditorialDAO ..> Editorial
    ReservacionDAO ..> Reservacion
    ConsolaVista ..> Stand
    ConsolaVista ..> Editorial
    ConsolaVista ..> Reservacion
    Stand ..> DatosInvalidosException
    Editorial ..> DatosInvalidosException
    Reservacion ..> DatosInvalidosException
    StandDAO ..> DatosInvalidosException
    EditorialDAO ..> DatosInvalidosException
    ReservacionDAO ..> DatosInvalidosException
    StandDAO ..> RegistroNoEncontradoException
    EditorialDAO ..> RegistroNoEncontradoException
    ReservacionDAO ..> RegistroNoEncontradoException
    ConexionBD ..> SQLException
    CrudController ..> SQLException : captura
    CrudController ..> DatosInvalidosException : captura
    CrudController ..> RegistroNoEncontradoException : captura
```

`ConexionBD` se encarga únicamente de la conexión y la inicialización del esquema. Cada DAO contiene las operaciones de una entidad. PagoDAO también valida el saldo y el plazo en una transacción JDBC. Los modelos no conocen JDBC y la vista no contiene SQL.
