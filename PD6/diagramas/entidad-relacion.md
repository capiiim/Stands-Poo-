# Diagrama entidad-relación PD6

```mermaid
erDiagram
    STANDS ||--o{ RESERVACIONES : tiene
    EDITORIALES ||--o{ RESERVACIONES : realiza
    RESERVACIONES ||--o{ PAGOS : recibe
    STANDS {
        int id PK
        text metros_cuadrados
        text precio_por_metro
        text estado
    }
    EDITORIALES {
        int id PK
        text nombre
        text correo
    }
    RESERVACIONES {
        int id PK
        int stand_id FK
        int editorial_id FK
        text fecha_reserva
        text fecha_limite
        text estado
        text costo_total
    }
    PAGOS {
        int id PK
        int reservacion_id FK
        text fecha_pago
        text monto
    }
```

Una reservación pertenece a una editorial y a un stand. Una reservación puede tener cero o varios pagos; cada pago corresponde a una sola reservación. No se repite el ID de editorial en pagos, ya que se obtiene mediante la reservación. Los importes se almacenan como texto decimal para conservar la precisión de BigDecimal.
