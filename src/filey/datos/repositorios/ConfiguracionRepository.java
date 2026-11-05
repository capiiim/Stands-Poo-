package filey.datos.repositorios;

import filey.datos.Configuracion;

public interface ConfiguracionRepository {
    Configuracion obtener();
    void guardar(Configuracion configuracion);
}
