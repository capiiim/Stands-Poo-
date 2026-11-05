package filey.negocio;

import filey.datos.*;
import filey.datos.repositorios.*;
import filey.negocio.eventos.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public final class VencimientoService {
    private final Object mutex;
    private final ReservaRepository reservas;
    private final StandRepository stands;
    private final PagoRepository pagos;
    private final ConfiguracionRepository parametros;
    private final GestorEstados gestor;
    private final Fechas fechas;
    private final PublicadorEventos eventos;

    public VencimientoService(Object mutex,ReservaRepository reservas,StandRepository stands,PagoRepository pagos,
                              ConfiguracionRepository parametros,GestorEstados gestor,Fechas fechas,
                              PublicadorEventos eventos) {
        this.mutex=mutex;this.reservas=reservas;this.stands=stands;this.pagos=pagos;
        this.parametros=parametros;this.gestor=gestor;this.fechas=fechas;this.eventos=eventos;
    }
    // Invocar desde tarea programada o manualmente; no existe un daemon permanente en esta demo.
    public int procesarReservasVencidas() {
        List<EventoReservaVencida> vencidas=new ArrayList<>();
        synchronized(mutex) {
            for (Reserva r:reservas.buscarVencidas(fechas.ahora())) {
                Stand s=stands.buscarPorId(r.getStandId()).orElseThrow();
                if (s.getEstado()!=EstadoStand.RESERVADO) continue;
                BigDecimal abonado=totalPagado(r.getId());
                BigDecimal minimo=r.getPrecioAcordado().multiply(parametros.obtener().getPorcentajeMinimo());
                if (abonado.compareTo(minimo)>=0) continue;
                gestor.cambiar(s.getId(),EstadoStand.DISPONIBLE,
                      new ContextoTransicion(false,true,"SISTEMA","Reserva #"+r.getId()+" vencida sin cubrir mínimo"));
                r.setEstado(EstadoReserva.VENCIDA);
                reservas.guardar(r);
                vencidas.add(new EventoReservaVencida(r.getId(),r.getEditorialId(),s.getId(),fechas.ahora()));
            }
        }
        vencidas.forEach(eventos::publicar);
        return vencidas.size();
    }
    public int procesarAvisos() {
        procesarReservasVencidas();
        List<EventoVencimientoProximo> avisos=new ArrayList<>();
        synchronized(mutex) {
            Configuracion c=parametros.obtener();
            LocalDateTime ahora=fechas.ahora();
            for (Reserva r:reservas.listarTodas()) {
                if (r.getEstado()!=EstadoReserva.VIGENTE) continue;
                Stand stand=stands.buscarPorId(r.getStandId()).orElseThrow();
                if (stand.getEstado()==EstadoStand.RESERVADO) {
                    anadirAviso(avisos,r,"RESERVA",r.getFechaVencimiento(),ahora,c);
                }
                if (totalPagado(r.getId()).compareTo(r.getTotalAPagar())<0) {
                    anadirAviso(avisos,r,"DESCUENTO",c.getFechaDescuento().atTime(23,59,59),ahora,c);
                    anadirAviso(avisos,r,"PAGO_TOTAL",c.getFechaPagoTotal().atTime(23,59,59),ahora,c);
                }
            }
        }
        avisos.forEach(eventos::publicar);
        return avisos.size();
    }
    private void anadirAviso(List<EventoVencimientoProximo> eventosSalida,Reserva reserva,String motivo,
                             LocalDateTime objetivo,LocalDateTime ahora,Configuracion c) {
        if (ahora.isBefore(objetivo) && !ahora.isBefore(objetivo.minus(c.getAnticipacionAviso()))) {
            eventosSalida.add(new EventoVencimientoProximo(reserva.getId(),reserva.getEditorialId(),
                    motivo,objetivo,ahora));
        }
    }
    private BigDecimal totalPagado(long id) {
        return pagos.listarPorReserva(id).stream().map(Pago::monto).reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
