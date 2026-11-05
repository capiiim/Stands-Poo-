package filey.datos.memoria;

import filey.datos.Configuracion;
import filey.datos.repositorios.ConfiguracionRepository;
import java.util.Objects;

public final class ConfiguracionRepositoryMemoria implements ConfiguracionRepository {
    private volatile Configuracion configuracion;
    public ConfiguracionRepositoryMemoria(Configuracion configuracion) {guardar(configuracion);}
    @Override public Configuracion obtener() {return configuracion;}
    @Override public void guardar(Configuracion config) {this.configuracion=Objects.requireNonNull(config);}
}
