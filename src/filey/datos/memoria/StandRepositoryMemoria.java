package filey.datos.memoria;

import filey.datos.*;
import filey.datos.repositorios.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class StandRepositoryMemoria implements StandRepository {
    private final Map<Long, Stand> datos = new ConcurrentHashMap<>();
    @Override public Stand guardar(Stand stand) {
        Stand copia = new Stand(stand);
        if (datos.putIfAbsent(copia.getId(), copia) != null)
            throw new IllegalArgumentException("El ID de stand ya existe y no se reutiliza");
        return new Stand(copia);
    }
    @Override public Optional<Stand> buscarPorId(long id) { return Optional.ofNullable(datos.get(id)).map(Stand::new); }
    @Override public List<Stand> listarPorEstado(EstadoStand estado) {
        return datos.values().stream().filter(s -> s.getEstado()==estado)
              .map(Stand::new).sorted(Comparator.comparingLong(Stand::getId)).toList();
    }
    @Override public List<Stand> listarTodos() {
        return datos.values().stream().map(Stand::new)
              .sorted(Comparator.comparingLong(Stand::getId)).toList();
    }
    @Override public boolean actualizarConVersion(Stand stand) {
        synchronized (datos) {
            Stand actual = datos.get(stand.getId());
            if (actual==null || actual.getVersion()!=stand.getVersion()) return false;
            Stand siguiente = new Stand(stand);
            siguiente.setVersion(actual.getVersion()+1);
            datos.put(siguiente.getId(), siguiente);
            return true;
        }
    }
}
