# Guía rápida para exponer FILEY

## Secuencia sugerida de demostración

1. Abrir una terminal en la carpeta del proyecto y ejecutar `java -jar FILEY.jar --demo`.
2. Explicar los dos stands, la editorial y la reserva #1 que se crean en memoria.
3. Mostrar que la segunda reserva del mismo stand es rechazada.
4. Mostrar el pago de $3,000 sobre $20,000 y cómo el stand pasa a `OCUPADO` cuando alcanza el **15 %**.
5. Mostrar cómo se obtiene el saldo de $17,000 y cómo el historial registra las dos transiciones.
6. Señalar las notificaciones `[CORREO SIMULADO]` y explicar Observer y el registro de auditoría.
7. Ejecutar `java -jar FILEY.jar` para abrir el menú, agregar datos o cambiar la configuración.

## Qué corresponde a cada paquete

- **Datos**: `Stand`, `EstadoStand`, `Editorial`, `Reserva`, `Pago`, `Configuracion`, `CambioEstado` y `RegistroNotificacion`, interfaces `Repository` e implementaciones `Memoria`.
- **Negocio**: `StandService`, `ReservaService`, `PagoService`, `VencimientoService`, `ConfiguracionService`, `ResumenService`, `GestorEstados`, `PrecioService` y autorización.
- **Notificaciones**: `BusEventos`, `ObservadorEmail`, `ObservadorAuditoria`, `CanalCorreo` y `ReintentoNotificacion`.

## Posibles preguntas del profesor

**¿Qué evita que se aparte dos veces el mismo stand?**

Las operaciones de reserva usan el mismo mutex y vuelven a comprobar el estado del stand y si hay una reserva vigente. La capa de datos también ofrece `actualizarConVersion` para detectar una versión obsoleta.

**¿Por qué se usa `BigDecimal` y no `double`?**

Para evitar errores binarios de representación al calcular dinero y aplicar redondeo uniforme.

**¿Qué sucede después del 15 %?**

El stand pasa a `OCUPADO`; todavía puede quedar saldo por pagar hasta la fecha de pago total.

**¿De qué sirve Observer?**

Los servicios crean eventos, mientras que el bus avisa a observadores de correo y auditoría. Cambiar el canal de correo no requiere editar la lógica de reservas o cobros.

**¿Envía correos de verdad?**

No, el canal es simulado; las llamadas y los registros de notificaciones son funcionales, pero los mensajes se imprimen en la terminal. La integración SMTP real no forma parte de esta entrega.

**¿La base de datos ya está implementada?**

No. La persistencia en memoria permite mostrar el diseño del backend; los datos se pierden al salir.

**¿Qué faltaría para producción?**

Base de datos y transacciones, outbox durable, envío SMTP real, despliegue y autenticación robusta.
