package filey.negocio;

import filey.datos.*;
import filey.datos.repositorios.*;
import filey.negocio.eventos.EventoReservaCreada;
import filey.negocio.excepciones.StandNoDisponibleException;
import java.math.BigDecimal;

public final class ReservaService {
    private final Object mutex;
    private final StandRepository stands;
    private final EditorialRepository editoriales;
    private final ReservaRepository reservas;
    private final PagoRepository pagos;
    private final ConfiguracionRepository parametros;
    private final GestorEstados gestor;
    private final AutorizacionService acceso;
    private final PublicadorEventos eventos;
    private final Fechas fechas;
    private final IdGenerador ids;
    public ReservaService(Object mutex,StandRepository stands,EditorialRepository editoriales,
                          ReservaRepository reservas,PagoRepository pagos,ConfiguracionRepository parametros,
                          GestorEstados gestor,AutorizacionService acceso,PublicadorEventos eventos,
                          Fechas fechas,IdGenerador ids) {
        this.mutex=mutex;this.stands=stands;this.editoriales=editoriales;this.reservas=reservas;
        this.pagos=pagos;this.parametros=parametros;this.gestor=gestor;
        this.acceso=acceso;this.eventos=eventos;this.fechas=fechas;this.ids=ids;
    }
    public Reserva reservar(String token,long standId,long editorialId) {
        String actor=acceso.exigirAdministrador(token);
        Reserva creada;
        synchronized(mutex) {
            Configuracion c=parametros.obtener();
            if (fechas.hoy().isAfter(c.getFechaLimiteReserva()))
                throw new IllegalStateException("Terminó la fecha límite para crear reservas");
            Stand stand=stands.buscarPorId(standId).orElseThrow(()->new IllegalArgumentException("Stand inexistente"));
            if (stand.getEstado()!=EstadoStand.DISPONIBLE || reservas.buscarVigentePorStand(standId).isPresent())
                throw new StandNoDisponibleException("El stand no está disponible para reserva");
            if (editoriales.buscarPorId(editorialId).isEmpty()) throw new IllegalArgumentException("Editorial inexistente");
            creada=new Reserva(ids.siguiente(),standId,editorialId,fechas.ahora(),
                    fechas.ahora().plus(c.getPlazoReserva()),stand.getPrecioTotal());
            // Mutex único para todas las escrituras del backend; no es una transacción SQL.
            gestor.cambiar(standId,EstadoStand.RESERVADO,new ContextoTransicion(false,false,actor,"Reserva #"+creada.getId()));
            reservas.guardar(creada);
        }
        eventos.publicar(new EventoReservaCreada(creada.getId(),editorialId,standId,fechas.ahora(),creada.getFechaVencimiento()));
        return creada;
    }
    public void cancelar(String token,long reservaId) {
        String actor=acceso.exigirAdministrador(token);
        synchronized(mutex) {
            Reserva r=reservas.buscarPorId(reservaId).orElseThrow(()->new IllegalArgumentException("Reserva inexistente"));
            if (r.getEstado()!=EstadoReserva.VIGENTE) throw new IllegalStateException("La reserva ya fue cerrada");
            if (!pagos.listarPorReserva(reservaId).isEmpty())
                throw new IllegalStateException("No se cancela una reserva con pagos: falta política de devolución");
            Stand stand=stands.buscarPorId(r.getStandId()).orElseThrow();
            if (stand.getEstado()!=EstadoStand.RESERVADO)
                throw new IllegalStateException("No se puede cancelar un stand ocupado");
            gestor.cambiar(r.getStandId(),EstadoStand.DISPONIBLE,new ContextoTransicion(false,true,actor,"Cancelación de reserva #"+reservaId));
            r.setEstado(EstadoReserva.CANCELADA);
            reservas.guardar(r);
        }
    }
}
