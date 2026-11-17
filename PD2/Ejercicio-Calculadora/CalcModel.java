import java.math.BigDecimal;

public class CalcModel {
    private BigDecimal total = BigDecimal.ONE;

    // BigDecimal permite trabajar con decimales muy largos sin usar double.
    public void multiplyBy(String numero) {
        total = total.multiply(new BigDecimal(numero.trim()));
    }

    public String getValue() {
        return total.stripTrailingZeros().toPlainString();
    }

    public void reset() {
        total = BigDecimal.ONE;
    }
}
