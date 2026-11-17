package filey.datos.repositorios;

import filey.datos.Editorial;
import java.util.*;

public interface EditorialRepository {
    Editorial guardar(Editorial editorial);
    Optional<Editorial> buscarPorId(long id);
    List<Editorial> listarTodos();
}
