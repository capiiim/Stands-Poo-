# FILEY — Sistema administrativo de venta de stands

## Primera entrega: análisis y diseño

Documentación inicial del backend para administrar stands de la Feria Internacional de la Lectura Yucatán (FILEY). Esta entrega contiene los requisitos y el diseño preliminar; **todavía no incluye una aplicación ejecutable**.

## Alcance

El sistema contempla registro y consulta de stands, cuatro estados (`Disponible`, `Reservado`, `Ocupado`, `Bloqueado`), reservas y abonos, descuento por pronto pago, configuración de fechas, vencimientos, notificaciones y trazabilidad. Incluye dos funciones opcionales de transferencia de stands y reservas.

## Requisitos

- [Requisitos funcionales (RF1–RF28)](requisitos/01-funcionales.md)
- [Requisitos no funcionales (RNF1–RNF20)](requisitos/02-no-funcionales.md)

## Diagramas

| Diagrama | Archivo |
|---|---|
| Clases: paquete `datos` | [01-clases-datos](diagramas/01-clases-datos.md) |
| Clases: paquete `negocio` | [02-clases-negocio](diagramas/02-clases-negocio.md) |
| Clases: paquete `notificaciones` | [03-clases-notificaciones](diagramas/03-clases-notificaciones.md) |
| Arquitectura por paquetes | [04-diagrama-paquetes](diagramas/04-diagrama-paquetes.md) |
| Máquina de estados | [05-diagrama-estados](diagramas/05-diagrama-estados.md) |
| Casos de uso | [06-casos-de-uso](diagramas/06-casos-de-uso.md) |
| Secuencia: reserva y pago | [07-secuencia-reserva-pago](diagramas/07-secuencia-reserva-pago.md) |
| Actividad: vencimientos | [08-actividad-vencimientos](diagramas/08-actividad-vencimientos.md) |

Los diagramas usan **Mermaid**, que GitHub puede mostrar al abrir estos archivos Markdown.

## Arquitectura propuesta

- **`datos`:** entidades, enumeraciones y contratos de repositorio.
- **`negocio`:** reglas de reservas, pagos, precios, estados y vencimientos.
- **`notificaciones`:** observadores, canales de envío y reintentos.

Decisiones de diseño: montos con `BigDecimal`, umbral mínimo de pago del 15 % configurable, control de concurrencia para evitar reservas dobles, zona horaria `America/Merida`, historial de solo anexado y credenciales fuera del código.

## Instalación, configuración y ejecución

Esta **primera entrega contiene documentación y diagramas**, no un backend ejecutable. No hay dependencias que instalar. Para revisarla, entra a GitHub y abre los documentos vinculados arriba. Las instrucciones de instalación, configuración y ejecución del software se agregarán cuando el backend esté implementado.

## Avance

- [x] Requisitos funcionales
- [x] Requisitos no funcionales
- [x] Diagramas de clases por paquete
- [x] Diagramas de arquitectura y procesos
- [ ] Implementación de clases
- [ ] Pruebas automatizadas
- [ ] Despliegue

## Aspectos a definir para implementar

- Precisar qué monto se debe pagar para recibir el descuento por pronto pago.
- Precisar políticas de reembolsos y cancelaciones después de haber pagado, que no están definidas todavía.
