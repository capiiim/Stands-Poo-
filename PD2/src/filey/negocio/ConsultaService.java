package filey.negocio;

import filey.datos.*;
import filey.datos.repositorios.*;
import java.util.*;

public final class ConsultaService {
    private final AutorizacionService acceso;
    private final StandRepository stands;
    private final EditorialRepository editoriales;
    private final ReservaRepository reservas;
    private final PagoRepository pagos;
    private final HistorialRepository historial;
    private final NotificacionRepository notificaciones;
    public ConsultaService(AutorizacionService acceso,StandRepository stands,EditorialRepository editoriales,
                           ReservaRepository reservas,PagoRepository pagos,HistorialRepository historial,
                           NotificacionRepository notificaciones) {
        this.acceso=acceso;this.stands=stands;this.editoriales=editoriales;this.reservas=reservas;
        this.pagos=pagos;this.historial=historial;this.notificaciones=notificaciones;
    }
    public List<Stand> stands(String token) {acceso.exigirAdministrador(token);return stands.listarTodos();}
    public List<Editorial> editoriales(String token) {acceso.exigirAdministrador(token);return editoriales.listarTodos();}
    public List<Reserva> reservas(String token) {acceso.exigirAdministrador(token);return reservas.listarTodas();}
    public List<Pago> pagos(String token,long idReserva) {acceso.exigirAdministrador(token);return pagos.listarPorReserva(idReserva);}
    public List<CambioEstado> historial(String token) {acceso.exigirAdministrador(token);return historial.listarTodos();}
    public List<RegistroNotificacion> notificaciones(String token) {acceso.exigirAdministrador(token);return notificaciones.listarTodos();}
}
