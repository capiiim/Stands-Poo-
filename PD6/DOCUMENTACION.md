# PD6 - Administración de pagos de reservaciones

## 1. Descripción

Se continuó el ejercicio PD5 de FILEY. Además del CRUD de stands, editoriales y reservaciones, ahora se pueden registrar abonos a una reservación, consultar los pagos de un mes y consultar los pagos de una reservación de una editorial determinada. Se conserva Java 17, MVC, JDBC y SQLite.

## 2. Separación de capas

- `modelo`: contiene los records Stand, Editorial, Reservacion y Pago, con validaciones de sus datos. No utiliza JDBC.
- `vista`: ConsolaVista muestra las entidades, los saldos y los mensajes. No ejecuta consultas SQL.
- `controlador`: CrudController coordina las operaciones de los cuatro DAO, presenta los resultados y captura las excepciones. Main crea las dependencias e inicia la demostración.
- `bd`: ConexionBD abre conexiones JDBC, activa las claves foráneas e inicializa las tablas. StandDAO, EditorialDAO, ReservacionDAO y PagoDAO realizan las consultas de su entidad mediante PreparedStatement y cierran los recursos con try-with-resources.
- `excepciones`: contiene las excepciones propias de validación, de registros no encontrados y de pagos inválidos.

## 3. Cambios respecto a PD5

Stand y Editorial mantienen sus atributos y operaciones. Reservacion agrega `costoTotal`, almacenado en `costo_total`. En el ejemplo se obtiene del precio del stand al reservar; se conserva como importe acordado aunque posteriormente cambie el precio del stand. El DAO permite ajustar ese importe antes de registrar pagos.

Se añade Pago con `id`, `reservacionId`, `fechaPago` y `monto`. La tabla pagos referencia a reservaciones por `reservacion_id`. La relación es de uno a muchos: una reservación puede no tener abonos o tener varios. Se conservan las relaciones previas con stands y editoriales. No se duplica editorial_id en pagos.

La BD de esta entrega es `filey_pd6.db`, independiente de PD5, y se genera al ejecutar el programa. No se migran datos de la BD anterior.

## 4. División de las operaciones CRUD

| Entidad | Clase | Create | Read | Update | Delete |
|---|---|---|---|---|---|
| Stand | StandDAO | crear | buscar, listar | actualizar | eliminar |
| Editorial | EditorialDAO | crear | buscar, listar | actualizar | eliminar |
| Reservacion | ReservacionDAO | crear | buscar, listar | actualizar | eliminar |
| Pago | PagoDAO | crear | buscar, listar, listarPorMes, listarPorReservacion, saldoPendiente | actualizar | eliminar |

Cada DAO recibe ConexionBD en su constructor. No hay SQL en el controlador ni en los modelos. Para Pago, `crear(Pago dato)` registra un abono y `actualizar(Pago dato)` corrige su fecha o monto, sin transferirlo a otra reservación. `eliminar(int id)` elimina un abono (corrección administrativa del ejercicio) y el saldo se recalcula al consultar. Las altas y modificaciones validan las reglas dentro de una transacción; si ocurre un error, se revierte la operación.

Consultas adicionales de PagoDAO:

- `listarPorMes(YearMonth mes)`: busca los pagos desde el primer día del mes indicado (incluido) hasta el primer día del siguiente mes (excluido). Se considera el año y el mes.
- `listarPorReservacion(int editorialId, int reservacionId)`: comprueba que la reservación pertenezca a esa editorial y usa JOIN para obtener sus abonos.
- `saldoPendiente(int reservacionId)`: costo total menos la suma de los montos de los pagos registrados. El saldo no se duplica en la BD.

## 5. Reglas de pagos

El importe debe ser positivo y tener como máximo dos decimales. Se usa BigDecimal, sin conversiones a double. La reservación debe existir, estar ACTIVA y la fecha del abono debe encontrarse entre la fecha de reserva y la fecha límite, ambas inclusive (se admite pagar durante el último día). No se permiten pagos superiores al saldo pendiente. Al corregir un pago, se excluye su monto anterior del cálculo para evitar contarlo dos veces. Cuando el saldo es cero, la reservación está liquidada en términos de pagos; no se cambia automáticamente su estado administrativo. Si el plazo termina con saldo positivo, queda sin liquidar y no se aceptan pagos con fecha posterior al límite. Las fechas corresponden al momento del abono registrado; la demostración utiliza fechas de ejemplo, no el reloj del sistema.

Para no alterar el historial de los abonos, una reservación con pagos no permite cambiar stand, editorial, fechas ni costo total. Las claves foráneas impiden borrar una reservación que todavía tenga pagos. En la demostración se eliminan primero los pagos de prueba y después las demás entidades. No se implementa un sistema contable real de cancelaciones, autenticación ni tareas automáticas de vencimiento, ya que no se solicitan en esta entrega.

## 6. Excepciones

Las excepciones propias están en `filey.pd6.excepciones`. SQLException pertenece a `java.sql` y se propaga desde la capa BD. CrudController captura estas excepciones y muestra el mensaje a través de ConsolaVista.

| Excepción y argumentos del constructor | Clase o método que la dispara | Motivo y argumentos de entrada |
|---|---|---|
| DatosInvalidosException(String mensaje) | Stand, Editorial, Reservacion, Pago (constructores) | ID no positivo; superficie o precio inválido; nombre o correo inválido; fechas o estado inválidos; costo o monto no positivo o con más de dos decimales; fecha de pago nula. |
| DatosInvalidosException(String mensaje) | DAO: crear/actualizar(dato), buscar/eliminar(int id), PagoDAO.listarPorMes(YearMonth mes), listarPorReservacion(int editorialId, int reservacionId), saldoPendiente(int reservacionId) | Dato nulo, identificadores no positivos o mes nulo. ReservacionDAO.actualizar(Reservacion dato) también rechaza cambios de los campos protegidos si existen abonos. |
| RegistroNoEncontradoException(String entidad, int id) | DAO: buscar(int id), actualizar(dato), eliminar(int id); PagoDAO.crear(Pago dato), listarPorReservacion(int editorialId, int reservacionId), saldoPendiente(int reservacionId) | No existe el registro indicado o la reservación no pertenece a la editorial solicitada. |
| PagoInvalidoException(int reservacionId, String motivo) | PagoDAO.crear(Pago dato), actualizar(Pago dato), mediante validarPago(Connection c, Pago dato, int excluirId) | Estado no activo, pago fuera de las fechas, monto superior al saldo o intento de cambiar la reservación de un pago. Hereda de DatosInvalidosException. |
| SQLException (mensaje del driver, SQLState y código según el error) | ConexionBD.conectar(), inicializar() y métodos JDBC de los DAO | Fallo de conexión, SQL, clave primaria repetida, clave foránea inexistente o eliminación restringida, entre otros. |

## 7. Demostración desde main()

`Main` ejecuta CrudController. Inicializa la BD, crea un stand de 10 m² a $2,000/m², una editorial y una reservación por $20,000. Busca y modifica las tres entidades, conservando el costo acordado. Registra dos abonos de $5,000 y $10,000 en noviembre y diciembre de 2026; consulta ambos meses y la reservación por editorial. El saldo esperado es $5,000. Corrige el segundo abono a $15,000, quedando saldo cero. Prueba un pago superior al saldo y uno fuera del plazo. Elimina los pagos de ejemplo y luego la reservación, editorial y stand de prueba. También demuestra las excepciones de ID no encontrado y datos inválidos.

El programa se puede ejecutar varias veces; utiliza IDs libres y elimina únicamente los registros que crea en una ejecución normal. Las fechas y cantidades de ejemplo están en el controlador para facilitar la siguiente modificación del ejercicio.

## 8. Diagramas

- [Diagrama de clases](diagramas/clases.md)
- [Diagrama entidad-relación](diagramas/entidad-relacion.md)
