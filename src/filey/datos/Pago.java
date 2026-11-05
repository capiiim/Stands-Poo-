package filey.datos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Pago(long id, long reservaId, BigDecimal monto, LocalDateTime fechaPago) {
    public Pago {
        if (id <= 0 || reservaId <= 0 || Objects.requireNonNull(monto).signum() <= 0)
            throw new IllegalArgumentException("Pago inválido");
        Objects.requireNonNull(fechaPago);
    }
}
