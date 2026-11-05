package filey.datos.repositorios;

import filey.datos.Reserva;
import java.time.LocalDateTime;
import java.util.*;

public interface ReservaRepository {
    Reserva guardar(Reserva reserva);
    Optional<Reserva> buscarPorId(long id);
    Optional<Reserva> buscarVigentePorStand(long standId);
    List<Reserva> buscarVencidas(LocalDateTime ahora);
    List<Reserva> listarTodas();
}
