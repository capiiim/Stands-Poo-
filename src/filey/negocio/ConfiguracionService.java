package filey.negocio;

import filey.datos.Configuracion;
import filey.datos.repositorios.ConfiguracionRepository;

public final class ConfiguracionService {
    private final Object mutex;
    private final ConfiguracionRepository repo;
    private final AutorizacionService acceso;
    public ConfiguracionService(Object mutex,ConfiguracionRepository repo,AutorizacionService acceso) {
        this.mutex=mutex;this.repo=repo;this.acceso=acceso;
    }
    public void actualizarParametros(String token,Configuracion nueva) {
        acceso.exigirAdministrador(token);
        synchronized(mutex) {repo.guardar(nueva);}
    }
    public Configuracion obtenerParametros(String token) {
        acceso.exigirAdministrador(token); return repo.obtener();
    }
}
