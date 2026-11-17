package filey.negocio;

import filey.datos.*;
import filey.datos.repositorios.*;
import filey.negocio.eventos.*;
import java.math.*;
import java.util.*;

public final class PagoService {
    private final Object mutex;
    private final PagoRepository pagos;
    private final ReservaRepository reservas;
    private final StandRepository stands;
    private final ConfiguracionRepository parametros;
    private final GestorEstados gestor;
    private final AutorizacionService acceso;
    private final PublicadorEventos eventos;
    private final PrecioService precios;
    private final Fechas fechas;
    private final IdGenerador ids;
    public PagoService(Object mutex,PagoRepository pagos,ReservaRepository reservas,StandRepository stands,
                       ConfiguracionRepository parametros,GestorEstados gestor,AutorizacionService acceso,
                       PublicadorEventos eventos,PrecioService precios,Fechas fechas,IdGenerador ids) {
        this.mutex=mutex;this.pagos=pagos;this.reservas=reservas;this.stands=stands;
        this.parametros=parametros;this.gestor=gestor;this.acceso=acceso;
        this.eventos=eventos;this.precios=precios;this.fechas=fechas;this.ids=ids;
    }
    public Pago registrarPago(String token,long reservaId,BigDecimal monto) {
        String actor=acceso.exigirAdministrador(token);
        Objects.requireNonNull(monto);
        monto=monto.setScale(2,RoundingMode.HALF_UP);
        if (monto.signum()<=0) throw new IllegalArgumentException("El abono debe ser positivo");
        Pago pago;
        EventoStandOcupado ocupacion=null;
        synchronized(mutex) {
            Reserva r=reservas.buscarPorId(reservaId).orElseThrow(()->new IllegalArgumentException("Reserva inexistente"));
            Configuracion c=parametros.obtener();
            if (r.getEstado()!=EstadoReserva.VIGENTE) throw new IllegalStateException("Reserva no vigente");
            Stand stand=stands.buscarPorId(r.getStandId()).orElseThrow();
            if (stand.getEstado()!=EstadoStand.RESERVADO && stand.getEstado()!=EstadoStand.OCUPADO)
                throw new IllegalStateException("El stand no corresponde a una reserva activa");
            if (stand.getEstado()==EstadoStand.RESERVADO && !fechas.ahora().isBefore(r.getFechaVencimiento()))
                throw new IllegalStateException("La reserva venció; primero procese vencimientos");
            if (fechas.hoy().isAfter(c.getFechaPagoTotal()))
                throw new IllegalStateException("Ya venció la fecha límite de pago total");
            BigDecimal pagado=totalPagado(reservaId);
            BigDecimal nuevoTotal=pagado.add(monto);
            BigDecimal objetivo=r.getTotalAPagar();
            BigDecimal descuento=BigDecimal.ZERO.setScale(2);
            if (r.getDescuentoAplicado().signum()==0 && !fechas.hoy().isAfter(c.getFechaDescuento())) {
                descuento=precios.calcularDescuento(r.getPrecioAcordado(),fechas.hoy(),c);
                objetivo=r.getPrecioAcordado().subtract(descuento);
            }
            if (nuevoTotal.compareTo(objetivo)>0)
                throw new IllegalArgumentException("El pago excede el saldo permitido ($"+objetivo.subtract(pagado)+")");
            boolean liquidado=nuevoTotal.compareTo(objetivo)==0;
            boolean llegoMinimo=nuevoTotal.compareTo(
                    r.getPrecioAcordado().multiply(c.getPorcentajeMinimo()))>=0;
            pago=new Pago(ids.siguiente(),reservaId,monto,fechas.ahora());
            // Todos los cambios de negocio comparten este mutex en la misma JVM.
            if (stand.getEstado()==EstadoStand.RESERVADO && llegoMinimo) {
                gestor.cambiar(stand.getId(),EstadoStand.OCUPADO,
                        new ContextoTransicion(true,false,actor,"Pago mínimo alcanzado, reserva #"+reservaId));
                ocupacion=new EventoStandOcupado(reservaId,r.getEditorialId(),stand.getId(),fechas.ahora());
            }
            pagos.registrar(pago);
            if (liquidado && descuento.signum()>0) {
                r.aplicarDescuento(descuento);
                reservas.guardar(r);
            }
        }
        eventos.publicar(new EventoPagoRegistrado(pago.id(),reservaId,
                reservas.buscarPorId(reservaId).orElseThrow().getEditorialId(),pago.monto(),fechas.ahora()));
        if (ocupacion!=null) eventos.publicar(ocupacion);
        return pago;
    }
    public BigDecimal calcularSaldo(String token,long reservaId) {
        acceso.exigirAdministrador(token);
        Reserva r=reservas.buscarPorId(reservaId).orElseThrow(()->new IllegalArgumentException("Reserva inexistente"));
        return r.getTotalAPagar().subtract(totalPagado(reservaId)).max(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
    }
    public BigDecimal porcentajeCubierto(String token,long reservaId) {
        acceso.exigirAdministrador(token);
        Reserva r=reservas.buscarPorId(reservaId).orElseThrow(()->new IllegalArgumentException("Reserva inexistente"));
        if (r.getPrecioAcordado().signum()==0) return BigDecimal.ONE;
        return totalPagado(reservaId).divide(r.getPrecioAcordado(),4,RoundingMode.HALF_UP);
    }
    public BigDecimal totalPagado(long reservaId) {
        return pagos.listarPorReserva(reservaId).stream().map(Pago::monto)
                .reduce(BigDecimal.ZERO.setScale(2),BigDecimal::add);
    }
}
