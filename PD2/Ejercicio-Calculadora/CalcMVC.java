import javax.swing.SwingUtilities;

public class CalcMVC {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalcModel modelo = new CalcModel();
            CalcView vista = new CalcView();
            new CalcController(modelo, vista);
            vista.setVisible(true);
        });
    }
}
