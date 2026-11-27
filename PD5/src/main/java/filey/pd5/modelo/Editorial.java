package filey.pd5.modelo;

import filey.pd5.excepciones.DatosInvalidosException;

public record Editorial(int id, String nombre, String correo) {
    public Editorial {
        if (id <= 0 || nombre == null || nombre.isBlank() || correo == null
                || !correo.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
            throw new DatosInvalidosException("ID, nombre o correo de editorial invalidos.");
    }
}
