# Diagramas del backend — Sistema FILEY

Esta carpeta contiene diagramas Mermaid que GitHub renderiza directamente al abrir los archivos `.md`.

| Archivo | Contenido |
|---|---|
| `01-clases-datos.md` | Entidades, enumeraciones y repositorios |
| `02-clases-negocio.md` | Servicios, reglas y puertos |
| `03-clases-notificaciones.md` | Observer, canales y reintentos |
| `04-diagrama-paquetes.md` | Dependencias y arquitectura |
| `05-diagrama-estados.md` | Transiciones permitidas del stand |
| `06-casos-de-uso.md` | Actores y funciones |
| `07-secuencia-reserva-pago.md` | Flujo de negocio principal |
| `08-actividad-vencimientos.md` | Revisión periódica de vencimientos |

## Decisiones de diseño

- Paquetes: `datos`, `negocio` y `notificaciones`.
- Arquitectura: interfaces de repositorio y publicación de eventos; el paquete de datos no depende de otros paquetes.
- Estado: `GestorEstados` con políticas de transición; historial de solo anexado.
- Notificaciones: Observer con publicación desacoplada y reintentos; se recomienda outbox transaccional.
- Dinero: `BigDecimal` con dos decimales y reglas de redondeo uniformes.
- Concurrencia: transacción y bloqueo por stand, o actualización optimista por `version`.
- Tiempo: `ZoneId` configurable, por defecto `America/Merida`.
- Reservas: el precio acordado debe permanecer estable para evitar que cambios posteriores modifiquen una compra ya registrada.
- Cobertura: RF1–RF28 y RNF1–RNF20, con transferencias RF24–RF25 marcadas como opcionales.

## Aclaraciones para implementación

1. Definir si el descuento se aplica al pagar el monto total antes de la fecha configurada, o a un porcentaje específico: el enunciado dice «monto correspondiente» sin detallarlo.
2. Definir qué ocurre si un pago se registra después del vencimiento de la reserva. El diseño propone validar que la reserva siga vigente.
3. Definir políticas de reembolso, devolución de pago, cancelación de stands ocupados y cierre de feria: no están especificadas, así que no se incluyeron transiciones nuevas.
