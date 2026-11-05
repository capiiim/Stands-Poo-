package filey.negocio;

import filey.datos.repositorios.ConfiguracionRepository;
import java.time.*;

public final class Fechas {
    private final ConfiguracionRepository configuracion;
    private final Clock reloj;
    public Fechas(ConfiguracionRepository configuracion, Clock reloj) {
        this.configuracion=configuracion; this.reloj=reloj;
    }
    public LocalDateTime ahora() {
        return LocalDateTime.ofInstant(reloj.instant(),configuracion.obtener().getZonaHoraria());
    }
    public LocalDate hoy() {return ahora().toLocalDate();}
}
