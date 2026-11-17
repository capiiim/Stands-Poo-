package filey.datos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Reserva {
    private final long id;
    private final long standId;
    private final long editorialId;
    private final LocalDateTime fechaReserva;
    private final LocalDateTime fechaVencimiento;
    private EstadoReserva estado;
    private final BigDecimal precioAcordado;
    private BigDecimal descuentoAplicado;

    public Reserva(long id, long standId, long editorialId, LocalDateTime fechaReserva,
                   LocalDateTime fechaVencimiento, BigDecimal precioAcordado) {
        if (id <= 0 || standId <= 0 || editorialId <= 0) throw new IllegalArgumentException("IDs inválidos");
        Objects.requireNonNull(fechaReserva); Objects.requireNonNull(fechaVencimiento);
        Objects.requireNonNull(precioAcordado);
        if (!fechaVencimiento.isAfter(fechaReserva) || precioAcordado.signum() < 0)
            throw new IllegalArgumentException("Plazo o precio de reserva inválido");
        this.id = id; this.standId = standId; this.editorialId = editorialId;
        this.fechaReserva = fechaReserva; this.fechaVencimiento = fechaVencimiento;
        this.precioAcordado = precioAcordado; this.estado = EstadoReserva.VIGENTE;
        this.descuentoAplicado = BigDecimal.ZERO.setScale(2);
    }
    public Reserva(Reserva r) {
        id=r.id; standId=r.standId; editorialId=r.editorialId;
        fechaReserva=r.fechaReserva; fechaVencimiento=r.fechaVencimiento;
        estado=r.estado; precioAcordado=r.precioAcordado; descuentoAplicado=r.descuentoAplicado;
    }
    public long getId() { return id; }
    public long getStandId() { return standId; }
    public long getEditorialId() { return editorialId; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public LocalDateTime getFechaVencimiento() { return fechaVencimiento; }
    public EstadoReserva getEstado() { return estado; }
    public BigDecimal getPrecioAcordado() { return precioAcordado; }
    public BigDecimal getDescuentoAplicado() { return descuentoAplicado; }
    public BigDecimal getTotalAPagar() { return precioAcordado.subtract(descuentoAplicado); }
    public void setEstado(EstadoReserva estado) { this.estado=Objects.requireNonNull(estado); }
    public void aplicarDescuento(BigDecimal descuento) {
        Objects.requireNonNull(descuento);
        if (descuento.signum() < 0 || descuento.compareTo(precioAcordado) > 0)
            throw new IllegalArgumentException("Descuento inválido");
        this.descuentoAplicado=descuento;
    }
    @Override public String toString() {
        return "Reserva " + id + " | stand=" + standId + " | editorial=" + editorialId +
               " | estado=" + estado + " | vence=" + fechaVencimiento +
               " | base=$" + precioAcordado + " | descuento=$" + descuentoAplicado;
    }
}
