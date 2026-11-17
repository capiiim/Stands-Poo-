import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CalcView extends JFrame {
    private final JTextField entrada = new JTextField("1", 12);
    private final JTextField resultado = new JTextField("1", 27);
    private final JButton multiplicar = new JButton("Multiplicar");
    private final JButton limpiar = new JButton("Limpiar");

    public CalcView() {
        super("Calculadora MVC");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        resultado.setEditable(false);
        panel.add(new JLabel("Numero:"));
        panel.add(entrada);
        panel.add(multiplicar);
        panel.add(new JLabel("Total:"));
        panel.add(resultado);
        panel.add(limpiar);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public String getUserInput() {
        return entrada.getText();
    }

    public void setTotal(String valor) {
        resultado.setText(valor);
    }

    public void reset() {
        entrada.setText("1");
        resultado.setText("1");
    }

    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void addMultiplyListener(ActionListener listener) {
        multiplicar.addActionListener(listener);
    }

    public void addClearListener(ActionListener listener) {
        limpiar.addActionListener(listener);
    }
}
