# Calculadora MVC

Ejemplo de calculadora en Java con Modelo, Vista y Controlador.
Multiplica el total por cada numero ingresado. Comienza en **1** (no en 0) para que las multiplicaciones funcionen desde el primer clic.
Usa `BigDecimal` para aceptar decimales muy largos sin perder precision por usar `double`.

## Ejecutar (PowerShell, dentro de esta carpeta)

```powershell
javac CalcMVC.java CalcModel.java CalcView.java CalcController.java
java CalcMVC
```

O abrir el JAR:

```powershell
java -jar CalculadoraMVC.jar
```

## Como usar

1. Escribe un numero en **Numero** (ejemplo: `2.5`).
2. Haz clic en **Multiplicar**. Cada clic multiplica el total actual por ese numero.
3. **Limpiar** reinicia el resultado a `1`.
4. Para decimales utiliza **punto** y no coma.

**MVC:** `CalcModel.java` realiza las cuentas, `CalcView.java` muestra la ventana y `CalcController.java` conecta botones y operaciones. `CalcMVC.java` inicia el programa.

Si ingresas `0`, el resultado pasa a `0` y seguira en `0` al multiplicar; eso es normal en matematicas. Presiona **Limpiar** para reiniciar.
