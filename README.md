# FILEY — Backend administrativo de stands (Java puro)

Proyecto de programación orientada a objetos para administrar los stands de la Feria Internacional de la Lectura Yucatán (FILEY). Implementación sin Spring, sin Maven y sin dependencias externas. Diseñado a partir de las **instrucciones originales** y los ocho diagramas Mermaid adjuntos.

## Inicio rápido en Windows (PowerShell)

**Requisito:** JDK 17 o superior (`java -version` y `javac -version`).

1. Extrae el ZIP en una carpeta normal (no ejecutes directamente desde el ZIP).
2. Abre PowerShell en la carpeta **FILEY_Java_Backend**.
3. Ejecuta el JAR ya compilado:

```powershell
java -jar .\FILEY.jar
```

Para ver el escenario automático, sin ingresar datos:

```powershell
java -jar .\FILEY.jar --demo
```

Si cambias el código fuente y quieres volver a compilarlo:

```powershell
powershell -ExecutionPolicy Bypass -File .\ejecutar.ps1
```

O manualmente:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$archivos = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 --release 17 -d out $archivos
java -cp out filey.Main
```

Si aparecen tildes incorrectas en PowerShell clásico, ejecuta `chcp 65001` antes del programa y usa una terminal UTF-8.

### En Linux / macOS

```bash
bash ejecutar.sh
# O, con JDK 17+: java -jar FILEY.jar --demo
```

## ¿Qué hace?

- Da de alta stands con ID, m², costo/m² y precio total.
- Administra editoriales con ID, nombre y correo.
- Gestiona estados del stand: `DISPONIBLE`, `RESERVADO`, `OCUPADO`, `BLOQUEADO`.
- Registra reservas con vencimiento configurable y precio congelado al reservar.
- Impide reservar un stand ya apartado o bloqueado, incluso con llamadas concurrentes dentro de la misma JVM.
- Registra abonos; al acumular **15 %** del precio base (configurable), pasa el stand a `OCUPADO`.
- Aplica descuento por liquidación dentro del plazo de pronto pago, con porcentaje configurable.
- Bloquea pagos fuera del plazo total, de reservas canceladas/vencidas y sobrepagos.
- Permite bloquear y liberar stands, y cancelar reservas sin pagos que aún no estén ocupadas.
- Revisa vencimientos de reserva y libera stands sin el pago mínimo.
- Genera avisos de vencimiento de reserva, pronto pago y pago total, con Observer, historial, deduplicación y reintentos.
- Permite configurar fechas, porcentaje mínimo, porcentaje de descuento, zona horaria y anticipación de avisos.
- Muestra resúmenes de stands, reservas, cobros e historial.
- Incluye autenticación **demostrativa**, con contraseña derivada mediante PBKDF2 y sesión en memoria.

## Datos que aparecen al abrirlo

Se precargan **dos stands** y **una editorial de prueba** para facilitar la demostración:

| Registro | Valor |
| --- | --- |
| Stand #1 | 10 m² × $2,000 = $20,000 |
| Stand #2 | 12 m² × $1,500 = $18,000 |
| Editorial #1 | Editorial Demo (`editorial@ejemplo.test`) |
| Usuario de prueba | `admin` |
| Contraseña de prueba | `Filey2026!` |

La consola inicia con la sesión de prueba abierta para simplificar la exposición. **Estas credenciales son públicas y no son aptas para producción.** Puedes usar los servicios Java directamente para autenticarte y aplicar permisos mediante tokens.

La configuración inicial se genera a partir del día en que se ejecuta el programa: fecha límite para reservar = hoy + 30 días, descuento = hoy + 14 días, pago total = hoy + 60 días, duración de reserva = 72 horas, umbral = 15 %, **descuento de demostración = 5 %**, anticipo de aviso = 24 horas y zona `America/Merida`.

## Estructura

```text
FILEY_Java_Backend/
├── README.md
├── DISEÑO_Y_LIMITACIONES.md
├── GUIA_DE_PRESENTACION.md
├── VERIFICACION.md
├── INSTRUCCIONES_ORIGINALES.docx
├── diagramas/               # 8 diagramas Mermaid originales, conservados
├── src/filey/Main.java      # Menú de consola y demo automática
├── src/filey/SistemaFiley.java
├── src/filey/datos/         # Entidades, enums y repositorios
│   ├── repositorios/        # Contratos (interfaces)
│   └── memoria/             # Persistencia temporal en memoria
├── src/filey/negocio/       # Servicios, autorizaciones, reglas de transición y eventos
│   ├── eventos/
│   └── excepciones/
├── src/filey/notificaciones/ # Observer, bus de eventos, correo simulado, reintentos
├── ejecutar.ps1
├── ejecutar.sh
└── FILEY.jar                # Aplicación ejecutable ya compilada
```

## Ciclo de vida de un stand

```text
DISPONIBLE ──reservar──> RESERVADO ──pago acumulado >= 15 %──> OCUPADO
    │                        │
    └──bloquear──> BLOQUEADO └──cancelación o vencimiento sin mínimo──> DISPONIBLE
                      │
                      └──liberar──> DISPONIBLE
```

Las transiciones no previstas son rechazadas por `GestorEstados`. **No** hay transición de salida desde `OCUPADO`, porque el enunciado no define devolución o cierre.

## Notificaciones

`VencimientoService.procesarAvisos()` y `procesarReservasVencidas()` se invocan desde el menú (opción 11) o desde otro programador. Son los métodos pedidos para producir avisos: **no hay un proceso automático ejecutándose permanentemente**.

`CanalCorreo` **SIMULA** el envío: imprime en pantalla el destinatario y el contenido. No envía mensajes reales ni requiere claves SMTP. Para conexión con correo real habría que sustituir esta implementación del puerto `CanalNotificacion`.

`RegistroNotificacion` conserva resultados, número de intentos y claves para no duplicar recordatorios. `ReintentoNotificacion` reprocesa los fallidos (menú 15). `ObservadorAuditoria` deja una segunda traza interna por evento (por eso aparecen dos registros, uno `AUDITORIA` y otro `CORREO`).

## Repositorio Git

El entregable solicitado dice «Registrar en un repo». Para subirlo a uno nuevo desde el directorio del proyecto:

```powershell
git init
git add .
git commit -m "Implementar backend Java FILEY con reservas, pagos y notificaciones"
```

Para subirlo a un repositorio que ya exista, **no** vuelvas a hacer `git init` dentro de él: copia la carpeta al repositorio, crea una rama y agrega los archivos normalmente. No se incluye conexión a GitHub ni se ha publicado automáticamente.

## Alcance

No se implementaron las funcionalidades opcionales de transferencia de reservas y asignación directa de stands bloqueados (`RF24/RF25` de los diagramas). Tampoco se implementaron pruebas unitarias automáticas, base de datos, interfaz web, SMTP real o infraestructura transaccional. Consulte `DISEÑO_Y_LIMITACIONES.md` antes de usar este código fuera de una demostración académica.
