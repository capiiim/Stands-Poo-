package filey.datos.memoria;

import filey.datos.Editorial;
import filey.datos.repositorios.EditorialRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class EditorialRepositoryMemoria implements EditorialRepository {
    private final Map<Long, Editorial> datos = new ConcurrentHashMap<>();
    @Override public Editorial guardar(Editorial editorial) {
        if (datos.putIfAbsent(editorial.id(), editorial)!=null) throw new IllegalArgumentException("Editorial ya registrada");
        return editorial;
    }
    @Override public Optional<Editorial> buscarPorId(long id) {return Optional.ofNullable(datos.get(id));}
    @Override public List<Editorial> listarTodos() {return datos.values().stream().sorted(Comparator.comparingLong(Editorial::id)).toList();}
}
