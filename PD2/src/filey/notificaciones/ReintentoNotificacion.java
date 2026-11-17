package filey.notificaciones;

import filey.datos.RegistroNotificacion;
import filey.datos.repositorios.NotificacionRepository;

public final class ReintentoNotificacion {
    private final NotificacionRepository registros;
    private final CanalNotificacion correo;
    public ReintentoNotificacion(NotificacionRepository registros,CanalNotificacion correo) {
        this.registros=registros;this.correo=correo;
    }
    public int reintentarPendientes() {
        int intentados=0;
        for (RegistroNotificacion r:registros.listarPendientes()) {
            if (!r.getCanal().equals("CORREO")) continue;
            ObservadorEmail.intentar(r,correo,registros);
            intentados++;
        }
        return intentados;
    }
}
