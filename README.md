# FILEY — Entregas PD2 y PD4

Repositorio organizado por entregas, sin cambiar el contenido original del proyecto.

## [PD2 — Sistema administrativo de stands](PD2/README.md)

Contiene el backend original en Java, requisitos funcionales y no funcionales, diagramas, ejecutable `FILEY.jar` y documentación de la entrega anterior.

**Ejecutar PD2** desde su carpeta:

```bash
cd PD2
java -jar FILEY.jar --demo
```

## [PD4 — MVC y JDBC](PD4/README.md)

Módulo independiente con patrón MVC, base SQLite, consultas JDBC para `Stand`, `Editorial` y `Reservacion`, y diagramas de clases y entidad-relación.

**Ejecutar PD4** desde su carpeta (requiere JDK 17 y Maven):

```bash
cd PD4
mvn compile exec:java
```

## Entrega

Para la PD4, entregar la URL de este repositorio. Falta grabar y publicar el video de máximo cinco minutos; agregar su URL a `PD4/README.md` (sección video).

> Nota: Los archivos fuente y documentación de PD2 se conservaron sin modificaciones. PD4 es una implementación separada para cumplir la consigna MVC + JDBC.
