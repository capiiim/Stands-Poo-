package filey.negocio;

import filey.datos.*;
import filey.datos.repositorios.*;
import filey.negocio.excepciones.TransicionInvalidaException;

public final class GestorEstados {
    private final StandRepository stands;
    private final HistorialRepository historial;
    private final ReglaTransicion politica;
    private final IdGenerador ids;
    private final Fechas fechas;
    public GestorEstados(StandRepository stands,HistorialRepository historial,ReglaTransicion politica,
                         IdGenerador ids,Fechas fechas) {
        this.stands=stands;this.historial=historial;this.politica=politica;this.ids=ids;this.fechas=fechas;
    }
    // Invocar dentro del mismo bloqueo de operaciones de negocio.
    public Stand cambiar(long standId, EstadoStand nuevo, ContextoTransicion contexto) {
        Stand stand=stands.buscarPorId(standId).orElseThrow(()->new IllegalArgumentException("Stand no existe"));
        EstadoStand anterior=stand.getEstado();
        if (!politica.permite(anterior,nuevo,contexto))
            throw new TransicionInvalidaException("Transición prohibida: "+anterior+" -> "+nuevo);
        stand.setEstado(nuevo);
        if (!stands.actualizarConVersion(stand)) throw new IllegalStateException("Conflicto de versión de stand");
        historial.anexar(new CambioEstado(ids.siguiente(),standId,anterior,nuevo,fechas.ahora(),
                contexto.actor(),contexto.motivo()));
        return stands.buscarPorId(standId).orElseThrow();
    }
    public boolean esTransicionValida(EstadoStand anterior,EstadoStand nuevo,ContextoTransicion contexto) {
        return politica.permite(anterior,nuevo,contexto);
    }
}
