package filey.notificaciones;

import filey.negocio.*;
import java.util.*;
import java.util.concurrent.*;

public final class BusEventos implements PublicadorEventos {
    private final Map<String,List<ObservadorEvento>> suscripciones=new ConcurrentHashMap<>();
    public void suscribir(String tipo,ObservadorEvento observador) {
        suscripciones.computeIfAbsent(tipo,k->new CopyOnWriteArrayList<>()).add(observador);
    }
    @Override public void publicar(EventoDominio evento) {
        for (ObservadorEvento observador:suscripciones.getOrDefault(evento.tipo(),List.of())) {
            try { observador.alRecibir(evento); }
            catch (RuntimeException ex) {System.err.println("Observer falló (negocio confirmado): "+ex.getMessage());}
        }
    }
}
