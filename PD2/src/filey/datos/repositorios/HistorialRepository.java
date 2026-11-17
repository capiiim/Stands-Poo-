package filey.datos.repositorios;

import filey.datos.CambioEstado;
import java.util.*;

public interface HistorialRepository {
    void anexar(CambioEstado cambio);
    List<CambioEstado> listarTodos();
}
