package filey.negocio;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerador {
    private final AtomicLong secuencia = new AtomicLong();
    public long siguiente() { return secuencia.incrementAndGet(); }
}
