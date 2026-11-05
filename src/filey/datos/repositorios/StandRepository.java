package filey.datos.repositorios;

import filey.datos.*;
import java.util.*;

public interface StandRepository {
    Stand guardar(Stand stand);
    Optional<Stand> buscarPorId(long id);
    List<Stand> listarPorEstado(EstadoStand estado);
    List<Stand> listarTodos();
    boolean actualizarConVersion(Stand stand);
}
