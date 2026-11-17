package filey.negocio.eventos;

import filey.negocio.EventoDominio;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventoPagoRegistrado(long pagoId,long reservaId,long editorialId,BigDecimal monto,
                                   LocalDateTime fechaHora) implements EventoDominio {
    public String tipo() {return "PAGO_REGISTRADO";}
    public String detalle() {return "Se recibió un pago de $"+monto+" para la reserva #"+reservaId;}
    public String claveUnica() {return tipo()+":"+pagoId;}
}
