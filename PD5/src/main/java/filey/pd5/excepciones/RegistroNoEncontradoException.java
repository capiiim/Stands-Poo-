package filey.pd5.excepciones;

public class RegistroNoEncontradoException extends Exception {
    public RegistroNoEncontradoException(String entidad, int id) {
        super(entidad + " con ID " + id + " no existe.");
    }
}
