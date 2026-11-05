package filey.datos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Stand {
    private final long id;
    private BigDecimal metrosCuadrados;
    private BigDecimal precioPorM2;
    private EstadoStand estado;
    private long version;

    public Stand(long id, BigDecimal metrosCuadrados, BigDecimal precioPorM2) {
        if (id <= 0) throw new IllegalArgumentException("ID de stand inválido");
        this.id = id;
        modificarDimensionesYPrecio(metrosCuadrados, precioPorM2);
        this.estado = EstadoStand.DISPONIBLE;
    }
    public Stand(Stand otro) {
        id = otro.id;
        metrosCuadrados = otro.metrosCuadrados;
        precioPorM2 = otro.precioPorM2;
        estado = otro.estado;
        version = otro.version;
    }
    public long getId() { return id; }
    public BigDecimal getMetrosCuadrados() { return metrosCuadrados; }
    public BigDecimal getPrecioPorM2() { return precioPorM2; }
    public BigDecimal getPrecioTotal() { return metrosCuadrados.multiply(precioPorM2).setScale(2, RoundingMode.HALF_UP); }
    public EstadoStand getEstado() { return estado; }
    public long getVersion() { return version; }
    public void modificarDimensionesYPrecio(BigDecimal m2, BigDecimal precio) {
        Objects.requireNonNull(m2); Objects.requireNonNull(precio);
        if (m2.signum() <= 0 || precio.signum() < 0) throw new IllegalArgumentException("M2 debe ser > 0; precio >= 0");
        metrosCuadrados = m2;
        precioPorM2 = precio.setScale(2, RoundingMode.HALF_UP);
    }
    public void setEstado(EstadoStand estado) { this.estado = Objects.requireNonNull(estado); }
    public void setVersion(long version) { if (version < 0) throw new IllegalArgumentException(); this.version = version; }
    @Override public String toString() { return "Stand " + id + " (" + metrosCuadrados + " m2, $" + getPrecioTotal() + ", " + estado + ", versión " + version + ")"; }
}
