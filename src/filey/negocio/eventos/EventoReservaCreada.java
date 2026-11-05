package filey.negocio.eventos;

import filey.negocio.EventoDominio;
import java.time.LocalDateTime;

public record EventoReservaCreada(long reservaId, long editorialId, long standId,
                                  LocalDateTime fechaHora, LocalDateTime vence) implements EventoDominio {
    public String tipo() { return "RESERVA_CREADA"; }
    public String detalle() {return "Se registró la reserva #"+reservaId+" para stand #"+standId+". Vence: "+vence;}
    public String claveUnica() {return tipo()+":"+reservaId;}
}
