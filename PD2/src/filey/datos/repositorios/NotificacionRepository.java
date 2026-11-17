package filey.datos.repositorios;

import filey.datos.RegistroNotificacion;
import java.util.*;

public interface NotificacionRepository {
    boolean anexarSiNoExiste(RegistroNotificacion registro);
    void actualizar(RegistroNotificacion registro);
    List<RegistroNotificacion> listarPendientes();
    List<RegistroNotificacion> listarTodos();
    boolean existeClave(String clave);
}
