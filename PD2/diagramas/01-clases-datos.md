# FILEY — Diagrama de clases: paquete `datos`

```mermaid
classDiagram
 direction LR
 class Stand {
   +Long id
   +BigDecimal metrosCuadrados
   +BigDecimal precioPorM2
   +BigDecimal precioTotal
   +EstadoStand estado
   +Long version
 }
 class EstadoStand {
   <<enumeration>>
   DISPONIBLE
   RESERVADO
   OCUPADO
   BLOQUEADO
 }
 class Editorial {
   +Long id
   +String nombre
   +String correo
 }
 class Reserva {
   +Long id
   +LocalDateTime fechaReserva
   +LocalDateTime fechaVencimiento
   +EstadoReserva estado
   +BigDecimal precioAcordado
   +BigDecimal descuentoAplicado
 }
 class EstadoReserva {
   <<enumeration>>
   VIGENTE
   CANCELADA
   VENCIDA
 }
 class Pago {
   +Long id
   +BigDecimal monto
   +LocalDateTime fechaPago
 }
 class Configuracion {
   +LocalDate fechaLimiteReserva
   +LocalDate fechaDescuento
   +LocalDate fechaPagoTotal
   +Duration plazoReserva
   +BigDecimal porcentajeMinimo = 0.15
   +BigDecimal porcentajeDescuento
   +Duration anticipacionAviso
   +ZoneId zonaHoraria
 }
 class CambioEstado {
   +Long id
   +EstadoStand anterior
   +EstadoStand nuevo
   +LocalDateTime fechaHora
   +String actor
   +String motivo
 }
 class RegistroNotificacion {
   +Long id
   +String tipoEvento
   +LocalDateTime fechaHora
   +String canal
   +String resultado
   +int intentos
 }
 class Administrador {
   +Long id
   +String usuario
   +String hashContrasena
   +String rol
 }
 class StandRepository {
   <<interface>>
   +guardar(Stand) Stand
   +buscarPorId(Long) Stand
   +listarPorEstado(EstadoStand) List~Stand~
   +actualizarConVersion(Stand) boolean
 }
 class ReservaRepository {
   <<interface>>
   +guardar(Reserva) Reserva
   +buscarVigentePorStand(Long) Reserva
   +buscarVencidas(LocalDateTime) List~Reserva~
 }
 class PagoRepository {
   <<interface>>
   +registrar(Pago) Pago
   +listarPorReserva(Long) List~Pago~
 }
 class ConfiguracionRepository {
   <<interface>>
   +obtener() Configuracion
   +guardar(Configuracion) void
 }
 class HistorialRepository {
   <<interface>>
   +anexar(CambioEstado) void
 }
 class NotificacionRepository {
   <<interface>>
   +anexar(RegistroNotificacion) void
 }
 Stand --> EstadoStand
 Reserva --> EstadoReserva
 Reserva "0..*" --> "1" Stand : para
 Reserva "0..*" --> "1" Editorial : de
 Pago "0..*" --> "1" Reserva : abono
 CambioEstado "0..*" --> "1" Stand : historial
 RegistroNotificacion "0..*" --> "1" Editorial : destinataria
 StandRepository ..> Stand
 ReservaRepository ..> Reserva
 PagoRepository ..> Pago
 ConfiguracionRepository ..> Configuracion
 HistorialRepository ..> CambioEstado
 NotificacionRepository ..> RegistroNotificacion
```

**Notas:** Los identificadores de stand son irreutilizables; `version` permite control de concurrencia optimista. Los pagos y el historial solo admiten inserciones, no edición o borrado. `precioAcordado` congela el importe base de una reserva para evitar alteraciones retroactivas.
