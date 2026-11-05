package filey.negocio.eventos;

import filey.negocio.EventoDominio;
import java.time.LocalDateTime;

public record EventoReservaVencida(long reservaId,long editorialId,long standId,
                                   LocalDateTime fechaHora) implements EventoDominio {
    public String tipo() {return "RESERVA_VENCIDA";}
    public String detalle() {return "La reserva #"+reservaId+" del stand #"+standId+" venció sin alcanzar el mínimo.";}
    public String claveUnica() {return tipo()+":"+reservaId;}
}
