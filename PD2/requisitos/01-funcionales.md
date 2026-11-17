# Requisitos Funcionales

**Proyecto:** Sistema administrativo de venta de stands (FILEY)

Los requisitos funcionales describen lo que el sistema deberá hacer para que el administrador gestione la venta de stands a las editoriales: catálogo de stands, ciclo de reserva y pago, fechas importantes parametrizables y notificaciones.

> Prioridad: **E** = esencial, **D** = deseable, **O** = opcional.

## 1. Gestión de stands

### RF1. Registro de stands (E)
El sistema deberá permitir al administrador registrar un stand indicando su identificador numérico, sus dimensiones en metros cuadrados (m²) y el precio por metro cuadrado.

### RF2. Identificador único (E)
El sistema deberá asignar a cada stand un identificador numérico único que no pueda repetirse ni reutilizarse.

### RF3. Cálculo del precio del stand (E)
El sistema deberá calcular automáticamente el precio total de un stand como el producto de sus m² por el precio por m², y recalcularlo si cambia alguno de los dos valores.

### RF4. Consulta de stands (E)
El sistema deberá permitir consultar el listado de stands y el detalle de cada uno (identificador, m², precio por m², precio total, estado y editorial asociada), con filtro por estado.

### RF5. Modificación de stands (D)
El sistema deberá permitir modificar las dimensiones y el precio por m² de un stand únicamente mientras esté en estado Disponible o Bloqueado.

## 2. Estados del stand

### RF6. Estados válidos (E)
El sistema deberá manejar para cada stand exactamente uno de los siguientes estados: **Disponible**, **Reservado**, **Ocupado** y **Bloqueado**.

### RF7. Transiciones de estado (E)
El sistema deberá permitir únicamente las transiciones válidas entre estados y rechazar las demás:

| Desde | Hacia | Condición |
|---|---|---|
| Disponible | Reservado | Una editorial solicita el stand |
| Disponible | Bloqueado | El administrador lo bloquea |
| Reservado | Ocupado | Se cubre el porcentaje mínimo de pago |
| Reservado | Disponible | Vence el tiempo límite de reserva o se cancela |
| Bloqueado | Disponible | El administrador lo libera |
| Bloqueado | Reservado | El administrador lo envía a una editorial (RF24, opcional) |

### RF8. Reserva de un stand (E)
El sistema deberá permitir reservar un stand Disponible a nombre de una editorial; al hacerlo, el stand pasa a estado Reservado y el sistema registra la fecha y hora de la reserva. Un stand que no esté Disponible no podrá reservarse.

### RF9. Tiempo límite de reserva (E)
El sistema deberá permitir configurar el tiempo límite que tiene una editorial para pagar un stand reservado. Si el plazo vence sin que se cubra el porcentaje mínimo, el sistema deberá liberar el stand y regresarlo al estado Disponible.

### RF10. Paso de Reservado a Ocupado (E)
El sistema deberá cambiar un stand de Reservado a Ocupado cuando los pagos acumulados de la editorial alcancen el porcentaje mínimo del precio total. El porcentaje mínimo será de 15 % por defecto y deberá ser configurable.

### RF11. Bloqueo de stands (E)
El sistema deberá permitir al administrador bloquear un stand Disponible para reservarlo a su conveniencia. Un stand Bloqueado no podrá ser reservado por las editoriales.

### RF12. Liberación de stands bloqueados (E)
El sistema deberá permitir al administrador regresar un stand Bloqueado al estado Disponible.

### RF13. Cancelación de reserva (D)
El sistema deberá permitir al administrador cancelar una reserva vigente, regresando el stand a Disponible y conservando el registro de la cancelación.

## 3. Reservas y pagos

### RF14. Registro de reservas (E)
El sistema deberá registrar por cada reserva el identificador de la editorial, el nombre de la editorial y el stand reservado.

### RF15. Gestión de editoriales (E)
El sistema deberá permitir registrar y consultar editoriales con, como mínimo, un identificador, su nombre y un medio de contacto (correo electrónico) para las notificaciones.

### RF16. Registro de pagos (E)
El sistema deberá permitir registrar los pagos (abonos) de una editorial asociados a su reserva, con monto y fecha, y conservar el historial de pagos.

### RF17. Saldo y porcentaje pagado (E)
El sistema deberá calcular, para cada reserva u ocupación, el monto pagado, el saldo pendiente y el porcentaje cubierto del precio total.

### RF18. Descuento por pronto pago (E)
El sistema deberá aplicar el descuento por pronto pago cuando la editorial cubra el monto correspondiente antes de la fecha de descuento configurada. El porcentaje de descuento deberá ser configurable.

## 4. Fechas importantes parametrizables

### RF19. Configuración de fechas (E)
El sistema deberá permitir al administrador configurar desde la sección de configuración, sin modificar el código, las siguientes fechas:
- Fecha de descuento por pronto pago.
- Fecha límite para cubrir el monto total del pago.
- Fecha límite de reserva.

### RF20. Aplicación de las fechas (E)
El sistema deberá usar las fechas configuradas para validar nuevas reservas (no se podrán reservar stands después de la fecha límite de reserva), para decidir el descuento por pronto pago y para determinar los vencimientos del pago total.

## 5. Notificaciones

### RF21. Notificación de vencimientos (E)
El sistema deberá notificar a las editoriales cuando se aproxime alguno de los siguientes eventos: el vencimiento de su reserva, el vencimiento del descuento por pronto pago o la fecha límite para cubrir el monto total. La anticipación con la que se notifica deberá ser configurable.

### RF22. Notificación de otros eventos (D)
El sistema deberá notificar a la editorial cuando su reserva sea registrada, cuando se registre un pago, cuando su reserva venza y el stand sea liberado, y cuando el stand pase a Ocupado.

### RF23. Registro de notificaciones (D)
El sistema deberá guardar un registro de las notificaciones enviadas (editorial, tipo, fecha y resultado del envío).

## 6. Transferencia de stands (opcional)

### RF24. Envío de un stand bloqueado a una editorial (O)
El sistema podrá permitir al administrador enviar directamente un stand Bloqueado a una editorial, cambiándolo a estado Reservado a nombre de esa editorial con su tiempo límite de reserva.

### RF25. Transferencia de reservas entre editoriales (O)
El sistema podrá permitir transferir una reserva vigente de una editorial a otra, conservando los pagos ya registrados y dejando constancia de la operación.

## 7. Administración y trazabilidad

### RF26. Acceso del administrador (E)
El sistema deberá permitir el acceso a las funciones administrativas únicamente a usuarios autenticados con el rol de administrador.

### RF27. Historial de cambios de estado (E)
El sistema deberá registrar cada cambio de estado de un stand (estado anterior, estado nuevo, fecha y hora, usuario o proceso que lo realizó y motivo).

### RF28. Resumen de ventas (D)
El sistema deberá mostrar al administrador un resumen con el número de stands por estado, los m² vendidos y el monto cobrado y pendiente de cobro.
