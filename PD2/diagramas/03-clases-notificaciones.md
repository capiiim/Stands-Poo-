# FILEY — Diagrama de clases: paquete `notificaciones`

```mermaid
classDiagram
 direction LR
 class EventoDominio {
   <<interface>>
   +tipo() String
   +fechaHora() LocalDateTime
 }
 class EventoReservaCreada
 class EventoPagoRegistrado
 class EventoReservaVencida
 class EventoStandOcupado
 class EventoVencimientoProximo
 class PublicadorEventos {
   <<interface>>
   +publicar(evento) void
 }
 class ObservadorEvento {
   <<interface>>
   +alRecibir(evento) void
 }
 class BusEventos {
   +suscribir(tipo, observador) void
   +publicar(evento) void
 }
 class ObservadorEmail {
   +alRecibir(evento) void
 }
 class ObservadorAuditoria {
   +alRecibir(evento) void
 }
 class CanalNotificacion {
   <<interface>>
   +enviar(destino, asunto, mensaje) ResultadoEnvio
 }
 class CanalCorreo {
   +enviar(destino, asunto, mensaje) ResultadoEnvio
 }
 class ReintentoNotificacion {
   +reintentarPendientes() void
 }
 class RegistroNotificacionRepository {
   <<interface>>
   +anexar(registro) void
   +listarPendientes() List~RegistroNotificacion~
 }
 EventoDominio <|.. EventoReservaCreada
 EventoDominio <|.. EventoPagoRegistrado
 EventoDominio <|.. EventoReservaVencida
 EventoDominio <|.. EventoStandOcupado
 EventoDominio <|.. EventoVencimientoProximo
 PublicadorEventos <|.. BusEventos
 BusEventos o--> ObservadorEvento : suscritos
 ObservadorEvento <|.. ObservadorEmail
 ObservadorEvento <|.. ObservadorAuditoria
 ObservadorEmail --> CanalNotificacion
 CanalNotificacion <|.. CanalCorreo
 ObservadorEmail --> RegistroNotificacionRepository
 ObservadorAuditoria --> RegistroNotificacionRepository
 ReintentoNotificacion --> RegistroNotificacionRepository
 ReintentoNotificacion --> CanalNotificacion
```

**Notas:** Observer desacopla el negocio de los canales. Se puede añadir `CanalWhatsApp` implementando `CanalNotificacion` sin cambiar servicios. Para que los errores de envío no reviertan reservas/pagos, persistir eventos pendientes dentro de la transacción de negocio (outbox) y distribuirlos después; registrar el resultado y reintentar cuando falle.
