# FILEY — Dependencias entre paquetes

```mermaid
flowchart LR
 A[Interfaz administrativa / API] --> B[negocio]
 B --> D[datos]
 B --> P[Puerto PublicadorEventos]
 P -.implementado por.-> N[notificaciones]
 N --> D
 T[Programador periódico] --> B
 X[(Base de datos)] <--> D
 N --> C[Correo electrónico]
```

`datos` no depende de `negocio` ni `notificaciones`. `negocio` publica eventos mediante un **puerto/interfaz**, sin depender de implementaciones de correo. `notificaciones` implementa ese puerto. La interfaz administrativa requiere autenticación con rol administrador.
