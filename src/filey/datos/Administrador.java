package filey.datos;

public record Administrador(long id, String usuario, String hashContrasena, String rol) {
    public Administrador {
        if (id<=0 || usuario==null || usuario.isBlank() || hashContrasena==null || rol==null)
            throw new IllegalArgumentException("Administrador inválido");
    }
}
