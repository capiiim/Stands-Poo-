package filey.negocio.excepciones;

public final class TransicionInvalidaException extends IllegalStateException {
    public TransicionInvalidaException(String mensaje) { super(mensaje); }
}
