package filey.datos.repositorios;

import filey.datos.Administrador;
import java.util.*;

public interface AdministradorRepository {
    void guardar(Administrador administrador);
    Optional<Administrador> buscarPorUsuario(String usuario);
    Optional<Administrador> buscarPorId(long id);
}
