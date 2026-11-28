package filey.pd6.modelo;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeParseException;
import java.util.Set;
import filey.pd6.excepciones.DatosInvalidosException;

public record Reservacion(int id, int standId, int editorialId, String fechaReserva, String fechaLimite, String estado, BigDecimal costoTotal) {
    public Reservacion {
        if (costoTotal == null || costoTotal.signum() <= 0)
            throw new DatosInvalidosException("El costo total debe ser positivo.");
        try { costoTotal = costoTotal.setScale(2, RoundingMode.UNNECESSARY); }
        catch (ArithmeticException ex) { throw new DatosInvalidosException("El costo total admite hasta dos decimales."); }
        if (id <= 0 || standId <= 0 || editorialId <= 0)
            throw new DatosInvalidosException("Los IDs deben ser positivos.");
        if (fechaReserva == null || fechaLimite == null)
            throw new DatosInvalidosException("Las fechas son obligatorias.");
        try {
            if (LocalDate.parse(fechaLimite).isBefore(LocalDate.parse(fechaReserva)))
                throw new DatosInvalidosException("La fecha limite no puede ser anterior a la reserva.");
        } catch (DateTimeParseException e) {
            throw new DatosInvalidosException("Las fechas deben ser validas (AAAA-MM-DD).");
        }
        if (estado == null || !Set.of("ACTIVA", "CANCELADA", "VENCIDA").contains(estado))
            throw new DatosInvalidosException("Estado de reservacion invalido.");
    }
}
