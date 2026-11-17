package filey.datos;

import java.time.LocalDateTime;

public record CambioEstado(long id, long standId, EstadoStand anterior, EstadoStand nuevo,
                           LocalDateTime fechaHora, String actor, String motivo) {}
