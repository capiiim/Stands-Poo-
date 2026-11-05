package filey.datos.memoria;

import filey.datos.RegistroNotificacion;
import filey.datos.repositorios.NotificacionRepository;
import java.util.*;

public final class NotificacionRepositoryMemoria implements NotificacionRepository {
    private final Map<Long,RegistroNotificacion> datos=new LinkedHashMap<>();
    private final Set<String> claves=new HashSet<>();
    @Override public synchronized boolean anexarSiNoExiste(RegistroNotificacion r) {
        if (!claves.add(r.getClaveUnica())) return false;
        datos.put(r.getId(),new RegistroNotificacion(r)); return true;
    }
    @Override public synchronized boolean existeClave(String clave) {return claves.contains(clave);}
    @Override public synchronized void actualizar(RegistroNotificacion registro) {
        if (!datos.containsKey(registro.getId())) throw new IllegalArgumentException("Notificación no existe");
        datos.put(registro.getId(),new RegistroNotificacion(registro));
    }
    @Override public synchronized List<RegistroNotificacion> listarPendientes() {
        return datos.values().stream().filter(r->r.getResultado().equals("PENDIENTE"))
                .map(RegistroNotificacion::new).toList();
    }
    @Override public synchronized List<RegistroNotificacion> listarTodos() {
        return datos.values().stream().map(RegistroNotificacion::new).toList();
    }
}
