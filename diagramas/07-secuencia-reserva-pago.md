# FILEY — Secuencia de reserva y pago

```mermaid
sequenceDiagram
 actor Admin as Administrador
 participant API as API / Auth
 participant RS as ReservaService
 participant DB as Repositorios / Transacción
 participant GE as GestorEstados
 participant EV as PublicadorEventos
 participant PS as PagoService
 Admin->>API: reservar(standId, editorialId)
 API->>API: Verificar rol administrador
 API->>RS: reservar(...)
 RS->>DB: Iniciar transacción y bloquear stand
 DB-->>RS: Stand y configuración
 alt Disponible y dentro de fecha límite
   RS->>GE: cambiar(Disponible, Reservado)
   GE->>DB: Guardar estado e historial
   RS->>DB: Guardar reserva y confirmar transacción
   RS->>EV: publicar(ReservaCreada)
   RS-->>API: Reserva registrada
 else No disponible o fuera de plazo
   RS->>DB: Revertir transacción
   RS-->>API: Error con motivo claro
 end
 Admin->>API: registrarPago(reservaId, monto)
 API->>PS: registrarPago(...)
 PS->>DB: Iniciar transacción y registrar abono
 PS->>DB: Consultar total de abonos y configuración
 alt Alcanzó porcentaje mínimo y estaba reservado
   PS->>GE: cambiar(Reservado, Ocupado)
   GE->>DB: Guardar estado e historial
 end
 PS->>DB: Confirmar transacción
 PS->>EV: publicar(PagoRegistrado)
 PS-->>API: Saldo y porcentaje actualizado
```

**Notas:** El publicador debe entregar eventos después del commit, con soporte de outbox para no perderlos. Las validaciones de descuentos y saldos usan `BigDecimal` con redondeo definido. Los abonos solo se registran en reservas válidas y el pago se procesa de manera atómica.
