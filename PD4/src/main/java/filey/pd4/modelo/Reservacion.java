package filey.pd4.modelo;
public record Reservacion(int id, int standId, int editorialId, String fechaReserva, String fechaLimite, String estado) {}
