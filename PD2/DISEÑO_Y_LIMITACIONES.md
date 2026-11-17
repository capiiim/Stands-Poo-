# Diseño del backend FILEY y decisiones explícitas

## Base documental

- `INSTRUCCIONES_ORIGINALES.docx`: enunciado aportado por el equipo.
- `diagramas/*.md`: nueve archivos del ZIP original (README y ocho diagramas), preservados sin modificar.

El enunciado pide un sistema de clases de backend para stands con dimensiones, precio por m², identificador, cuatro estados, plazos configurables y notificaciones Observer. Los diagramas proponen clases de datos, negocio, notificaciones, control de estados, repositorios y eventos.

## Separación de paquetes

- `filey.datos`: entidades inmutables o copias defensivas, enumeraciones, interfaces de repositorios e implementaciones en memoria.
- `filey.negocio`: lógica de reservas, cobros, control de transiciones, autorización, configuración, vencimientos, resúmenes y eventos (interfaz `PublicadorEventos`).
- `filey.notificaciones`: bus de eventos Observer, canal simulado, auditoría y reintentos.
- `filey.Main` y `filey.SistemaFiley`: consola de presentación y composición de dependencias.

`negocio` no necesita importar implementaciones concretas de notificaciones: solo el puerto `PublicadorEventos`. Los repositorios se inyectan mediante interfaces.

## Reglas aplicadas

| Tema | Regla elegida |
| --- | --- |
| Precio | `BigDecimal`, centavos con redondeo `HALF_UP`. Se congela al reservar. |
| Estado de stand | Exclusivamente las transiciones del diagrama 05. |
| Ocupación | Se activa al cubrir al menos `porcentajeMinimo` del **precio base** (15 % por defecto). |
| Reserva | Debe crearse antes o en la fecha límite de reserva; vence `plazoReserva` después de su creación. |
| Vencimiento | Si llega el vencimiento y aún no se cubrió el mínimo, la reserva pasa a VENCIDA y su stand a DISPONIBLE cuando se invoque el procesador. |
| Cancelación | Solo con reserva vigente, stand RESERVADO y **sin abonos**, ya que faltan reglas de reembolso. |
| Pago | Solo con reserva VIGENTE, dentro del plazo total y (si está RESERVADO) antes de que expire la reserva. Abonos exclusivamente positivos. |
| Descuento | **Interpretación académica:** descuento por pronto pago al **liquidar el total** a más tardar la fecha de descuento. No se aplica a abonos que aún no completan la liquidación. |
| Monto del descuento | El Word no indica un porcentaje; se usa **5 % únicamente como ejemplo configurable**. |
| Sobrepago | Se rechaza para evitar generar devoluciones no especificadas. Dentro del plazo de descuento, el máximo abonable es el precio base menos descuento si todavía no se ha liquidado. |
| Fecha de pago total | Puede pagarse hasta el final de ese día; después se rechazan nuevos abonos, pero **no** se libera automáticamente un stand OCUPADO. |
| Notificaciones | Se avisa si el vencimiento está en la ventana `anticipacionAviso`. El recordatorio de un mismo plazo/fecha se guarda una sola vez. |
| Horarios | `ZoneId` configurable, default `America/Merida`; fechas de descuento y pago total cierran 23:59:59 hora local. |
| Autorización | Operaciones administrativas requieren un token de sesión válido en memoria. La consola inicia con una cuenta demostrativa. |

## Patrones utilizados

1. **Repository:** acceso a entidades mediante interfaces, con implementaciones en memoria.
2. **State / política de transiciones:** `GestorEstados` y `PoliticaTransiciones` centralizan las transiciones permitidas y dejan traza.
3. **Observer:** `BusEventos` publica y `ObservadorEmail`/`ObservadorAuditoria` reaccionan sin acoplar servicios de negocio al canal.
4. **Strategy:** `CanalNotificacion` permite reemplazar el mecanismo de correo sin tocar el negocio.
5. **Control de concurrencia:** `StandRepositoryMemoria.actualizarConVersion` usa número de versión. Un mutex compartido serializa las operaciones compuestas de los servicios para evitar dobles reservas en esta JVM.

## Advertencias técnicas y límites

**Persistencia:** toda la información se pierde al cerrar el programa. No hay base de datos. No existe transacción ACID multi-repositorio: los cambios están coordinados por un mutex dentro de una misma JVM, adecuado para demostración, no para múltiples servidores.

**Outbox:** se publican los eventos después de actualizar el estado de negocio, y los fallos de correo se registran como pendientes cuando el observer logra ejecutarse. No hay una outbox durable transaccional; un corte del proceso entre el guardado y la publicación podría perder notificaciones. El diagrama la **recomienda**, pero no se afirma que esté implementada.

**Correo:** `CanalCorreo` solo simula el envío por salida estándar. El resultado `ENVIADO` significa «enviado por el canal simulado», **no** que haya llegado un mensaje real.

**Concurrencia:** el bloqueo compartido cubre los servicios de esta instancia. No se garantiza seguridad si otros clientes usan los repositorios directamente o si hay múltiples instancias del sistema.

**Seguridad:** el login, aunque deriva claves PBKDF2, es una maqueta de consola; usa una contraseña pública de prueba, no tiene límites de intentos, expiración de sesiones, auditoría de accesos ni mecanismos para red.

**Reglas no especificadas:** no se inventaron devoluciones de abonos, cesión de espacios, levantamiento de `OCUPADO`, bloqueo retroactivo, cobro automático, cancelación de stand ocupado ni cierre general de feria. Cuando una reserva vence sin mínimo, los posibles abonos históricos permanecen anotados; no se define devolución.

**Registro Git:** los archivos están preparados para registrarse en un repositorio; no se publican remotamente por sí solos.

**Pruebas:** se realizaron verificaciones técnicas durante la preparación, pero **no** se agregó un marco de pruebas automatizadas como parte del entregable por elección del equipo.
