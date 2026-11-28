package filey.pd6.excepciones;

public class PagoInvalidoException extends DatosInvalidosException {
    public PagoInvalidoException(int reservacionId, String motivo) {
        super("Reservacion " + reservacionId + ": " + motivo);
    }
}
