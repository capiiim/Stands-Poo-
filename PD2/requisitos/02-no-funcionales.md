# Requisitos No Funcionales

**Proyecto:** Sistema administrativo de venta de stands (FILEY)

Los requisitos no funcionales describen las cualidades y restricciones que debe cumplir el sistema.

## Arquitectura y mantenibilidad

### RNF1. Organización en paquetes
El código deberá organizarse en tres paquetes con responsabilidades separadas: **datos** (entidades y acceso a datos), **lógica de negocio** (reglas de reserva, pago y cambio de estado) y **notificaciones**. El paquete de datos no deberá depender de los otros dos, y la lógica de negocio no deberá conocer los detalles de cada canal de notificación.

### RNF2. Patrón Observer en notificaciones
El mecanismo de notificación deberá implementarse con el patrón Observer: la lógica de negocio publica eventos (reserva por vencer, pronto pago por vencer, fecha límite por vencer) y los observadores suscritos ejecutan la acción correspondiente. Agregar un nuevo canal de notificación (por ejemplo, WhatsApp) no deberá requerir modificar la lógica de negocio.

### RNF3. Extensibilidad de estados
El manejo de estados y transiciones deberá estar centralizado en un solo componente (por ejemplo, patrón State o una tabla de transiciones) para poder agregar estados o reglas sin modificar el resto del sistema.

### RNF4. Bajo acoplamiento y alta cohesión
Las clases deberán tener una única responsabilidad y comunicarse mediante interfaces, de modo que puedan probarse y reemplazarse de forma independiente.

### RNF5. Pruebas
La lógica de negocio (cálculo de precios, transiciones de estado, porcentaje mínimo, vencimientos y descuento) deberá contar con pruebas unitarias automatizadas.

## Configurabilidad

### RNF6. Parámetros externos al código
Las fechas importantes, el tiempo límite de reserva, el porcentaje mínimo para pasar a Ocupado (15 % por defecto), el porcentaje de descuento y la anticipación de las notificaciones deberán almacenarse como configuración editable, sin necesidad de recompilar ni redesplegar el sistema.

## Integridad y confiabilidad

### RNF7. Consistencia ante concurrencia
Dos usuarios no deberán poder reservar el mismo stand al mismo tiempo. Si se intenta, solo una operación tendrá éxito y la otra recibirá un mensaje claro de que el stand ya no está disponible.

### RNF8. Precisión monetaria
Los montos y porcentajes deberán manejarse con tipos de dato decimales de precisión exacta (por ejemplo, `BigDecimal`), nunca con tipos de punto flotante, y redondearse con un criterio uniforme a dos decimales.

### RNF9. Verificación periódica de vencimientos
El sistema deberá revisar automáticamente y de forma periódica las reservas y fechas límite para liberar stands vencidos y disparar las notificaciones, aun si ningún usuario está usando el sistema.

### RNF10. Tolerancia a fallos de notificación
Un fallo al enviar una notificación no deberá impedir la operación de negocio que la originó. El sistema deberá registrar el error y permitir el reintento.

### RNF11. Historial no alterable
El historial de cambios de estado y de pagos deberá conservarse y no podrá editarse ni eliminarse desde la interfaz.

## Seguridad

### RNF12. Autenticación y autorización
Las funciones administrativas deberán requerir autenticación, y las contraseñas deberán almacenarse cifradas con un algoritmo de hash seguro (nunca en texto plano).

### RNF13. Credenciales fuera del código
Las credenciales de la base de datos y de los servicios de notificación deberán leerse de variables de entorno o de un archivo de configuración excluido del repositorio.

### RNF14. Protección de datos de contacto
Los datos de contacto de las editoriales solo deberán ser visibles para usuarios autorizados y solo se usarán para las notificaciones del sistema.

## Rendimiento y usabilidad

### RNF15. Tiempo de respuesta
Las consultas y operaciones habituales (listar stands, reservar, registrar un pago) deberán responder en menos de 2 segundos con el volumen esperado de la feria (alrededor de cientos de stands y editoriales).

### RNF16. Usabilidad
La interfaz administrativa deberá mostrar el estado de cada stand de forma visible (por ejemplo, con colores) y confirmar con un mensaje claro cada operación exitosa o fallida.

### RNF17. Zona horaria
Todas las fechas y horas deberán manejarse con una misma zona horaria definida en la configuración (por defecto, la de Yucatán, UTC−6) para evitar errores en los vencimientos.

## Portabilidad y operación

### RNF18. Portabilidad
El sistema deberá poder ejecutarse en cualquier equipo con la versión requerida del entorno de ejecución, sin depender de un sistema operativo específico.

### RNF19. Registro de eventos
El sistema deberá generar registros (logs) de errores y de las operaciones críticas (reservas, pagos, cambios de estado y notificaciones) para facilitar su diagnóstico.

### RNF20. Documentación
El proyecto deberá incluir un README con las instrucciones de instalación, configuración y ejecución, y diagramas de clases por paquete.
