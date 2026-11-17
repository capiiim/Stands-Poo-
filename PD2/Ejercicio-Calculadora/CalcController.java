public class CalcController {
    private final CalcModel modelo;
    private final CalcView vista;

    public CalcController(CalcModel modelo, CalcView vista) {
        this.modelo = modelo;
        this.vista = vista;

        vista.addMultiplyListener(e -> multiplicar());
        vista.addClearListener(e -> limpiar());
    }

    private void multiplicar() {
        try {
            modelo.multiplyBy(vista.getUserInput());
            vista.setTotal(modelo.getValue());
        } catch (NumberFormatException e) {
            vista.showError("Ingresa un numero valido. Usa punto para los decimales.");
        }
    }

    private void limpiar() {
        modelo.reset();
        vista.reset();
    }
}
