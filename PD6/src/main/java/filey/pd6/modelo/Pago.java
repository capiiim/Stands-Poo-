package filey.pd6.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import filey.pd6.excepciones.DatosInvalidosException;

public record Pago(int id, int reservacionId, LocalDate fechaPago, BigDecimal monto) {
    public Pago {
        if (id <= 0 || reservacionId <= 0 || fechaPago == null || monto == null || monto.signum() <= 0)
            throw new DatosInvalidosException("IDs, fecha y monto del pago deben ser validos.");
        try { monto = monto.setScale(2, RoundingMode.UNNECESSARY); }
        catch (ArithmeticException ex) {
            throw new DatosInvalidosException("El monto admite hasta dos decimales.");
        }
    }
}
