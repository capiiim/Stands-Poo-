package filey.negocio;

import filey.datos.*;
import filey.datos.repositorios.*;
import java.math.BigDecimal;
import java.util.*;

public final class ResumenService {
    private final StandRepository stands;
    private final ReservaRepository reservas;
    private final PagoRepository pagos;
    private final AutorizacionService acceso;
    private final Object mutex;
    public ResumenService(Object mutex,StandRepository stands,ReservaRepository reservas,
                         PagoRepository pagos,AutorizacionService acceso) {
        this.mutex=mutex;this.stands=stands;this.reservas=reservas;this.pagos=pagos;this.acceso=acceso;
    }
    public Resumen resumenStands(String token) {return resumen(token);}
    public Resumen resumenCobros(String token) {return resumen(token);}
    private Resumen resumen(String token) {
        acceso.exigirAdministrador(token);
        synchronized(mutex) {
            List<Stand> ss=stands.listarTodos();
            List<Reserva> rr=reservas.listarTodas();
            BigDecimal base=BigDecimal.ZERO,cobrado=BigDecimal.ZERO,pendiente=BigDecimal.ZERO;
            for (Reserva r:rr) {
                base=base.add(r.getPrecioAcordado());
                BigDecimal sumado=pagos.listarPorReserva(r.getId()).stream().map(Pago::monto)
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
                cobrado=cobrado.add(sumado);
                if (r.getEstado()==EstadoReserva.VIGENTE)
                    pendiente=pendiente.add(r.getTotalAPagar().subtract(sumado).max(BigDecimal.ZERO));
            }
            return new Resumen(contar(ss,EstadoStand.DISPONIBLE),contar(ss,EstadoStand.RESERVADO),
                    contar(ss,EstadoStand.OCUPADO),contar(ss,EstadoStand.BLOQUEADO),
                    contar(rr,EstadoReserva.VIGENTE),contar(rr,EstadoReserva.VENCIDA),
                    contar(rr,EstadoReserva.CANCELADA),base,cobrado,pendiente);
        }
    }
    private static long contar(List<Stand> ss,EstadoStand estado) {
        return ss.stream().filter(s->s.getEstado()==estado).count();
    }
    private static long contar(List<Reserva> rr,EstadoReserva estado) {
        return rr.stream().filter(r->r.getEstado()==estado).count();
    }
}
