# FILEY — Diagrama de estados de un stand

```mermaid
stateDiagram-v2
 [*] --> Disponible : registro
 Disponible --> Reservado : reservar / antes de fecha límite
 Disponible --> Bloqueado : bloqueo administrativo
 Bloqueado --> Disponible : liberación administrativa
 Bloqueado --> Reservado : asignación directa (opcional)
 Reservado --> Ocupado : pagos acumulados >= umbral mínimo
 Reservado --> Disponible : cancelación o vencimiento sin mínimo pagado
```

**Reglas:** El mínimo es configurable (15 % por defecto). `Ocupado` no tiene transición de salida definida en los requisitos. El motor central rechaza cualquier transición no dibujada. Una reserva no puede iniciarse después de la fecha límite configurada.
