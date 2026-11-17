package filey.notificaciones;

import filey.datos.RegistroNotificacion;
import filey.datos.repositorios.NotificacionRepository;
import filey.negocio.*;

public final class ObservadorAuditoria implements ObservadorEvento {
    private final NotificacionRepository registros;
    private final IdGenerador ids;
    private final Fechas fechas;
    public ObservadorAuditoria(NotificacionRepository registros,IdGenerador ids,Fechas fechas) {
        this.registros=registros;this.ids=ids;this.fechas=fechas;
    }
    @Override public void alRecibir(EventoDominio evento) {
        RegistroNotificacion r=new RegistroNotificacion(ids.siguiente(),evento.editorialId(),evento.tipo(),
                evento.claveUnica()+":AUDITORIA","AUDITORIA","registro interno",evento.tipo(),
                evento.detalle(),fechas.ahora());
        if (registros.anexarSiNoExiste(r)) {r.registrarResultado(true);registros.actualizar(r);}
    }
}
