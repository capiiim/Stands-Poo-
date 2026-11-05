package filey.negocio;

import filey.datos.Configuracion;
import java.math.*;
import java.time.LocalDate;

public final class PrecioService {
    public BigDecimal calcularTotal(BigDecimal m2,BigDecimal precioM2) {
        if (m2.signum()<=0 || precioM2.signum()<=0) throw new IllegalArgumentException("Precio/m2 inválido");
        return m2.multiply(precioM2).setScale(2,RoundingMode.HALF_UP);
    }
    public BigDecimal calcularDescuento(BigDecimal total, LocalDate fecha, Configuracion config) {
        if (fecha.isAfter(config.getFechaDescuento())) return BigDecimal.ZERO.setScale(2);
        return total.multiply(config.getPorcentajeDescuento()).setScale(2,RoundingMode.HALF_UP);
    }
}
