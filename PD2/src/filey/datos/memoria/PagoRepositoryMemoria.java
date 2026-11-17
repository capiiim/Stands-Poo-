package filey.datos.memoria;

import filey.datos.Pago;
import filey.datos.repositorios.PagoRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class PagoRepositoryMemoria implements PagoRepository {
    private final Map<Long,Pago> datos=new ConcurrentHashMap<>();
    @Override public Pago registrar(Pago pago) {
        if (datos.putIfAbsent(pago.id(),pago)!=null) throw new IllegalArgumentException("Pago ya existe");
        return pago;
    }
    @Override public List<Pago> listarPorReserva(long reservaId) {
        return datos.values().stream().filter(p->p.reservaId()==reservaId)
                .sorted(Comparator.comparingLong(Pago::id)).toList();
    }
    @Override public List<Pago> listarTodos() {return datos.values().stream().sorted(Comparator.comparingLong(Pago::id)).toList();}
}
