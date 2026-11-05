package filey.negocio;

import filey.datos.EstadoStand;

public final class PoliticaTransiciones implements ReglaTransicion {
    @Override public boolean permite(EstadoStand actual, EstadoStand nuevo, ContextoTransicion c) {
        return switch (actual) {
            case DISPONIBLE -> nuevo==EstadoStand.RESERVADO || nuevo==EstadoStand.BLOQUEADO;
            case BLOQUEADO -> nuevo==EstadoStand.DISPONIBLE; // asignación directa: opcional, no habilitada
            case RESERVADO -> (nuevo==EstadoStand.OCUPADO && c.minimoCubierto()) ||
                             (nuevo==EstadoStand.DISPONIBLE && c.cierreReserva());
            case OCUPADO -> false;
        };
    }
}
