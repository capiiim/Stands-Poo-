package filey.pd5.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;
import filey.pd5.excepciones.DatosInvalidosException;

public record Reservacion(int id, int standId, int editorialId, String fechaReserva, String fechaLimite, String estado) {
    public Reservacion {
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
