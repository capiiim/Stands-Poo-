package filey.negocio.eventos;

import filey.negocio.EventoDominio;
import java.time.LocalDateTime;

public record EventoStandOcupado(long reservaId,long editorialId,long standId,
                                 LocalDateTime fechaHora) implements EventoDominio {
    public String tipo() {return "STAND_OCUPADO";}
    public String detalle() {return "El stand #"+standId+" de la reserva #"+reservaId+" alcanzó el pago mínimo.";}
    public String claveUnica() {return tipo()+":"+reservaId;}
}
