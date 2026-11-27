# FILEY — Entregas PD2, PD4 y PD5

Repositorio organizado por entregas, conservando las versiones anteriores.

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

## [PD5 — MVC + BD (CRUD)](PD5/README.md)

Continuación de PD4 con operaciones CRUD para Stand, Editorial y Reservacion, separación de conexión y DAO, excepciones y diagramas.

**Ejecutar PD5** desde su carpeta (requiere JDK 17 y Maven):

```bash
cd PD5
mvn compile exec:java
```

## Entrega

La documentación, diagramas y código de PD5 se encuentran en la carpeta `PD5`. El enlace al video de máximo cinco minutos se agrega en `PD5/README.md`.