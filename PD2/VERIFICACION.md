# Verificación técnica del entregable

Se compilaron todos los archivos `.java` con `javac --release 17 -encoding UTF-8` y se ejecutó `filey.Main --demo`.

Además se ejecutó un arnés de verificación **externo** al proyecto (no se incluye como suite de tests de la entrega), con 34 aserciones correctas relacionadas con:

- Registro de precio y rechazo de acceso sin autorización.
- Bloqueo, liberación y rechazo de doble reserva.
- Correos pendientes, reintentos y no duplicación de eventos.
- Pago parcial debajo del mínimo y ocupación al llegar exactamente al 15 %.
- Saldos, porcentajes, descuentos de pronto pago y prevención de sobrepagos.
- Avisos previos, deduplicación de avisos, vencimiento y liberación del stand.
- Cancelación de reservas sin pagos e historial de estados.
- Actualización optimista y rechazo de versiones obsoletas.
- Simulación de 10 intentos concurrentes de reservar un mismo stand, con una sola reserva exitosa.

Las pruebas corroboran escenarios concretos y **no** sustituyen una revisión completa de requisitos o un entorno de producción.
