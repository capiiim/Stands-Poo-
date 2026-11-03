# FILEY — Diagrama de casos de uso

```mermaid
flowchart LR
 Admin[Administrador] --> Login([Autenticarse])
 Admin --> Gestion([Gestionar stands y editoriales])
 Admin --> Reservar([Registrar o cancelar reservas])
 Admin --> Pago([Registrar pagos])
 Admin --> Config([Configurar fechas y porcentajes])
 Admin --> Reporte([Consultar resumen e historial])
 Admin --> Opcional([Transferir reservas o asignar bloqueado - opcional])
 Reloj[Programador automático] --> Venc([Revisar vencimientos])
 Venc --> Liberar([Liberar reservas vencidas])
 Venc --> Avisar([Generar avisos de vencimiento])
 Reservar --> Evento([Publicar eventos])
 Pago --> Evento
 Avisar --> Evento
 Evento --> Correo([Enviar notificación])
 Correo --> Editorial[Editorial destinataria]
```

**Nota:** El diagrama representa actores y funcionalidades principales en un formato compatible con GitHub; las editoriales son destinatarias de avisos, no usuarios administrativos según estos requisitos.
