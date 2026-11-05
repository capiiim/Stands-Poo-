package filey.negocio.eventos;

import filey.negocio.EventoDominio;
import java.time.LocalDateTime;

public record EventoVencimientoProximo(long reservaId,long editorialId,String motivo,
                                       LocalDateTime objetivo,LocalDateTime fechaHora) implements EventoDominio {
    public String tipo() {return "VENCIMIENTO_PROXIMO";}
    public String detalle() {return "Aviso de "+motivo+" para la reserva #"+reservaId+". Fecha límite: "+objetivo;}
    public String claveUnica() {return tipo()+":"+reservaId+":"+motivo+":"+objetivo;}
}
