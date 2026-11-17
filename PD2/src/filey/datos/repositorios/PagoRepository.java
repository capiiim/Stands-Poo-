package filey.datos.repositorios;

import filey.datos.Pago;
import java.util.*;

public interface PagoRepository {
    Pago registrar(Pago pago);
    List<Pago> listarPorReserva(long reservaId);
    List<Pago> listarTodos();
}
