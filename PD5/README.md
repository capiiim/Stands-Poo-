# FILEY — PD5: MVC + BD (CRUD)

Continuación de PD4. Se mantienen las entidades Stand, Editorial y Reservacion, el patrón MVC y la conexión SQLite mediante JDBC. Ahora cada entidad tiene operaciones de alta, lectura, modificación y baja, además de manejo de excepciones.

### Enlace al video

[Ver video explicativo de la PD5](https://drive.google.com/drive/folders/1Np_BKkEoZPuEMbttyBomKiXqKNAZciyP?usp=drive_link)

## Requisitos y ejecución

Java JDK 17 o superior y Maven 3.8 o superior. La primera ejecución requiere Internet para descargar las dependencias.

Desde la carpeta `PD5`:

```bash
mvn compile exec:java
```

El programa crea `filey_pd5.db`, comprueba la conexión y muestra las operaciones CRUD para las tres entidades, incluyendo ejemplos de excepciones. Los registros de demostración se eliminan al terminar. La base de datos de PD4 no se modifica.

## Documentación y diagramas

- [Documentación de lo implementado, estrategia CRUD y excepciones](DOCUMENTACION.md)
- [Diagrama de clases](diagramas/clases.md)
- [Diagrama entidad-relación](diagramas/entidad-relacion.md)
- [Esquema de la base de datos](bd/esquema.sql)
