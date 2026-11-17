package filey.datos.memoria;

import filey.datos.CambioEstado;
import filey.datos.repositorios.HistorialRepository;
import java.util.*;

public final class HistorialRepositoryMemoria implements HistorialRepository {
    private final List<CambioEstado> cambios=new ArrayList<>();
    @Override public synchronized void anexar(CambioEstado cambio) {cambios.add(cambio);}
    @Override public synchronized List<CambioEstado> listarTodos() {return List.copyOf(cambios);}
}
