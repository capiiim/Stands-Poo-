package filey.negocio;

public record ContextoTransicion(boolean minimoCubierto, boolean cierreReserva, String actor, String motivo) {}
