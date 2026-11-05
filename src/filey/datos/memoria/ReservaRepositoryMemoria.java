package filey.datos.memoria;

import filey.datos.*;
import filey.datos.repositorios.ReservaRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ReservaRepositoryMemoria implements ReservaRepository {
    private final Map<Long, Reserva> datos=new ConcurrentHashMap<>();
    @Override public Reserva guardar(Reserva reserva) {
        datos.put(reserva.getId(), new Reserva(reserva));
        return new Reserva(reserva);
    }
    @Override public Optional<Reserva> buscarPorId(long id) {return Optional.ofNullable(datos.get(id)).map(Reserva::new);}
    @Override public Optional<Reserva> buscarVigentePorStand(long standId) {
        return datos.values().stream().filter(r->r.getStandId()==standId && r.getEstado()==EstadoReserva.VIGENTE)
                .findFirst().map(Reserva::new);
    }
    @Override public List<Reserva> buscarVencidas(LocalDateTime ahora) {
        return datos.values().stream().filter(r->r.getEstado()==EstadoReserva.VIGENTE && !r.getFechaVencimiento().isAfter(ahora))
                .map(Reserva::new).toList();
    }
    @Override public List<Reserva> listarTodas() {
        return datos.values().stream().map(Reserva::new)
                .sorted(Comparator.comparingLong(Reserva::getId)).toList();
    }
}
