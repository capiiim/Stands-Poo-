package filey.datos;

public record Editorial(long id, String nombre, String correo) {
    public Editorial {
        if (id <= 0 || nombre == null || nombre.isBlank() || correo == null || !correo.contains("@"))
            throw new IllegalArgumentException("Datos de editorial inválidos");
        nombre = nombre.trim(); correo = correo.trim();
    }
}
