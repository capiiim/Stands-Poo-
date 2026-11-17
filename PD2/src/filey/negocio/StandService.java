package filey.negocio;

import filey.datos.*;
import filey.datos.repositorios.*;
import java.math.BigDecimal;
import java.util.List;

public final class StandService {
    private final Object mutex;
    private final StandRepository stands;
    private final GestorEstados gestor;
    private final AutorizacionService acceso;
    private final IdGenerador ids;
    private final PrecioService precios;
    public StandService(Object mutex,StandRepository stands,GestorEstados gestor,
                        AutorizacionService acceso,IdGenerador ids,PrecioService precios) {
        this.mutex=mutex;this.stands=stands;this.gestor=gestor;this.acceso=acceso;this.ids=ids;this.precios=precios;
    }
    public Stand crearStand(String token,BigDecimal m2,BigDecimal precioM2) {
        acceso.exigirAdministrador(token);
        precios.calcularTotal(m2,precioM2);
        synchronized(mutex) {return stands.guardar(new Stand(ids.siguiente(),m2,precioM2));}
    }
    public Stand modificarStand(String token,long id,BigDecimal m2,BigDecimal precioM2) {
        acceso.exigirAdministrador(token); precios.calcularTotal(m2,precioM2);
        synchronized(mutex) {
            Stand s=stands.buscarPorId(id).orElseThrow(()->new IllegalArgumentException("Stand no existe"));
            if (s.getEstado()!=EstadoStand.DISPONIBLE) throw new IllegalStateException("Solo puede modificar stands disponibles");
            s.modificarDimensionesYPrecio(m2,precioM2);
            if (!stands.actualizarConVersion(s)) throw new IllegalStateException("Conflicto de versión");
            return stands.buscarPorId(id).orElseThrow();
        }
    }
    public List<Stand> listar(String token,EstadoStand estado) {
        acceso.exigirAdministrador(token);
        return estado==null?stands.listarTodos():stands.listarPorEstado(estado);
    }
    public void bloquear(String token,long id) {
        String actor=acceso.exigirAdministrador(token);
        synchronized(mutex) {gestor.cambiar(id,EstadoStand.BLOQUEADO,new ContextoTransicion(false,false,actor,"Bloqueo administrativo"));}
    }
    public void liberarBloqueado(String token,long id) {
        String actor=acceso.exigirAdministrador(token);
        synchronized(mutex) {gestor.cambiar(id,EstadoStand.DISPONIBLE,new ContextoTransicion(false,false,actor,"Liberación administrativa"));}
    }
}
