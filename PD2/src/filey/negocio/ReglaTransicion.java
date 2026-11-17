package filey.negocio;

import filey.datos.EstadoStand;

public interface ReglaTransicion {
    boolean permite(EstadoStand actual, EstadoStand nuevo, ContextoTransicion contexto);
}
