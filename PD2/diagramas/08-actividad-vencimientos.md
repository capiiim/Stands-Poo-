# FILEY — Flujo de vencimientos y avisos

```mermaid
flowchart TD
 Inicio([Ejecución programada]) --> Leer[Leer configuración, zona horaria y reservas]
 Leer --> Siguiente{¿Quedan reservas?}
 Siguiente -- No --> Fin([Fin])
 Siguiente -- Sí --> Vigente{¿Reserva vigente?}
 Vigente -- No --> Siguiente
 Vigente -- Sí --> Plazo{¿Venció el plazo sin alcanzar el mínimo?}
 Plazo -- Sí --> Liberar[Transacción: marcar vencida y liberar stand]
 Liberar --> Historial[Guardar cambio de estado y evento]
 Historial --> Siguiente
 Plazo -- No --> Proximidad{¿Algún vencimiento próximo?}
 Proximidad -- Sí --> Evento[Publicar evento: reserva, descuento o pago total]
 Evento --> Notificar[Observer envía y registra resultado]
 Notificar --> Error{¿Fallo el envío?}
 Error -- Sí --> Pendiente[Marcar pendiente para reintento]
 Error -- No --> Siguiente
 Pendiente --> Siguiente
 Proximidad -- No --> Siguiente
```

**Notas:** Los plazos se calculan con la zona horaria configurada; una entrega fallida no revierte cambios de negocio. Se evita enviar recordatorios duplicados del mismo tipo para el mismo vencimiento.
