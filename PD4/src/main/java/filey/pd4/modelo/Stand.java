package filey.pd4.modelo;
import java.math.BigDecimal;
public record Stand(int id, BigDecimal metrosCuadrados, BigDecimal precioPorMetro, String estado) {
 public BigDecimal precioTotal() { return metrosCuadrados.multiply(precioPorMetro).setScale(2, java.math.RoundingMode.HALF_UP); }
}
