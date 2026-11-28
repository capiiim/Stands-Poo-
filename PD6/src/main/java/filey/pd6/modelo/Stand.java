package filey.pd6.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import filey.pd6.excepciones.DatosInvalidosException;

public record Stand(int id, BigDecimal metrosCuadrados, BigDecimal precioPorMetro, String estado) {
    public Stand {
        if (id <= 0 || metrosCuadrados == null || metrosCuadrados.signum() <= 0
                || precioPorMetro == null || precioPorMetro.signum() <= 0)
            throw new DatosInvalidosException("ID, superficie y precio deben ser positivos.");
        if (estado == null || !Set.of("DISPONIBLE", "RESERVADO", "OCUPADO", "BLOQUEADO").contains(estado))
            throw new DatosInvalidosException("Estado de stand invalido.");
    }
    public BigDecimal precioTotal() {
        return metrosCuadrados.multiply(precioPorMetro).setScale(2, RoundingMode.HALF_UP);
    }
}
