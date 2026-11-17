package filey.datos;

import java.math.BigDecimal;
import java.time.*;
import java.util.Objects;

public final class Configuracion {
    private final LocalDate fechaLimiteReserva, fechaDescuento, fechaPagoTotal;
    private final Duration plazoReserva, anticipacionAviso;
    private final BigDecimal porcentajeMinimo, porcentajeDescuento;
    private final ZoneId zonaHoraria;

    public Configuracion(LocalDate fechaLimiteReserva, LocalDate fechaDescuento, LocalDate fechaPagoTotal,
                         Duration plazoReserva, BigDecimal porcentajeMinimo, BigDecimal porcentajeDescuento,
                         Duration anticipacionAviso, ZoneId zonaHoraria) {
        this.fechaLimiteReserva = Objects.requireNonNull(fechaLimiteReserva);
        this.fechaDescuento = Objects.requireNonNull(fechaDescuento);
        this.fechaPagoTotal = Objects.requireNonNull(fechaPagoTotal);
        this.plazoReserva = Objects.requireNonNull(plazoReserva);
        this.porcentajeMinimo = Objects.requireNonNull(porcentajeMinimo);
        this.porcentajeDescuento = Objects.requireNonNull(porcentajeDescuento);
        this.anticipacionAviso = Objects.requireNonNull(anticipacionAviso);
        this.zonaHoraria = Objects.requireNonNull(zonaHoraria);
        if (plazoReserva.isZero() || plazoReserva.isNegative() || anticipacionAviso.isNegative())
            throw new IllegalArgumentException("Duraciones inválidas");
        if (porcentajeMinimo.compareTo(BigDecimal.ZERO) <= 0 || porcentajeMinimo.compareTo(BigDecimal.ONE) > 0 ||
            porcentajeDescuento.signum() < 0 || porcentajeDescuento.compareTo(BigDecimal.ONE) >= 0)
            throw new IllegalArgumentException("Porcentajes fuera de rango");
        if (fechaDescuento.isAfter(fechaPagoTotal) || fechaLimiteReserva.isAfter(fechaPagoTotal))
            throw new IllegalArgumentException("Las fechas de descuento/reserva no deben superar la de pago total");
    }
    public static Configuracion demostracion(LocalDate hoy) {
        return new Configuracion(hoy.plusDays(30), hoy.plusDays(14), hoy.plusDays(60),
                Duration.ofDays(3), new BigDecimal("0.15"), new BigDecimal("0.05"),
                Duration.ofHours(24), ZoneId.of("America/Merida"));
    }
    public LocalDate getFechaLimiteReserva() { return fechaLimiteReserva; }
    public LocalDate getFechaDescuento() { return fechaDescuento; }
    public LocalDate getFechaPagoTotal() { return fechaPagoTotal; }
    public Duration getPlazoReserva() { return plazoReserva; }
    public BigDecimal getPorcentajeMinimo() { return porcentajeMinimo; }
    public BigDecimal getPorcentajeDescuento() { return porcentajeDescuento; }
    public Duration getAnticipacionAviso() { return anticipacionAviso; }
    public ZoneId getZonaHoraria() { return zonaHoraria; }
    @Override public String toString() {
        return "Última reserva=" + fechaLimiteReserva + ", descuento=" + fechaDescuento +
                ", pago total=" + fechaPagoTotal + ", plazo=" + plazoReserva +
                ", mínimo=" + porcentajeMinimo + ", descuento=" + porcentajeDescuento +
                ", aviso=" + anticipacionAviso + ", zona=" + zonaHoraria;
    }
}
