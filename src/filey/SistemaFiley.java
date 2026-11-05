package filey;

import filey.datos.*;
import filey.datos.repositorios.*;
import filey.datos.memoria.*;
import filey.negocio.*;
import filey.notificaciones.*;
import java.time.*;

/** Punto de composición del backend; no requiere framework ni base de datos. */
public final class SistemaFiley {
    public final AutorizacionService autorizacion;
    public final StandService stands;
    public final EditorialService editoriales;
    public final ReservaService reservas;
    public final PagoService pagos;
    public final VencimientoService vencimientos;
    public final ConfiguracionService configuracion;
    public final ResumenService resumenes;
    public final ConsultaService consultas;
    public final ReintentoNotificacion reintentos;
    private final BusEventos bus;

    public SistemaFiley(Clock reloj,Configuracion parametros,CanalNotificacion canalCorreo) {
        // Mutex único: protege operaciones compuestas dentro de un solo proceso Java.
        Object mutex=new Object();
        StandRepository standRepo=new StandRepositoryMemoria();
        EditorialRepository editorialRepo=new EditorialRepositoryMemoria();
        ReservaRepository reservaRepo=new ReservaRepositoryMemoria();
        PagoRepository pagoRepo=new PagoRepositoryMemoria();
        ConfiguracionRepository configRepo=new ConfiguracionRepositoryMemoria(parametros);
        HistorialRepository historiaRepo=new HistorialRepositoryMemoria();
        NotificacionRepository notifRepo=new NotificacionRepositoryMemoria();
        AdministradorRepository adminRepo=new AdministradorRepositoryMemoria();
        Fechas fechas=new Fechas(configRepo,reloj);
        autorizacion=new AutorizacionService(adminRepo);
        bus=new BusEventos();
        IdGenerador idEventos=new IdGenerador();
        ObservadorEmail email=new ObservadorEmail(editorialRepo,notifRepo,canalCorreo,idEventos,fechas);
        ObservadorAuditoria auditoria=new ObservadorAuditoria(notifRepo,idEventos,fechas);
        for (String tipo:new String[]{"RESERVA_CREADA","PAGO_REGISTRADO","RESERVA_VENCIDA",
                                      "STAND_OCUPADO","VENCIMIENTO_PROXIMO"}) {
            bus.suscribir(tipo,auditoria);
            bus.suscribir(tipo,email);
        }
        reintentos=new ReintentoNotificacion(notifRepo,canalCorreo);
        PrecioService precios=new PrecioService();
        GestorEstados gestor=new GestorEstados(standRepo,historiaRepo,new PoliticaTransiciones(),new IdGenerador(),fechas);
        stands=new StandService(mutex,standRepo,gestor,autorizacion,new IdGenerador(),precios);
        editoriales=new EditorialService(editorialRepo,autorizacion,new IdGenerador());
        reservas=new ReservaService(mutex,standRepo,editorialRepo,reservaRepo,pagoRepo,configRepo,
                gestor,autorizacion,bus,fechas,new IdGenerador());
        pagos=new PagoService(mutex,pagoRepo,reservaRepo,standRepo,configRepo,
                gestor,autorizacion,bus,precios,fechas,new IdGenerador());
        vencimientos=new VencimientoService(mutex,reservaRepo,standRepo,pagoRepo,configRepo,gestor,fechas,bus);
        configuracion=new ConfiguracionService(mutex,configRepo,autorizacion);
        resumenes=new ResumenService(mutex,standRepo,reservaRepo,pagoRepo,autorizacion);
        consultas=new ConsultaService(autorizacion,standRepo,editorialRepo,reservaRepo,pagoRepo,historiaRepo,notifRepo);
    }
    public static SistemaFiley crearDemostracion() {
        Clock reloj=Clock.systemUTC();
        ZoneId zona=ZoneId.of("America/Merida");
        return new SistemaFiley(reloj,Configuracion.demostracion(LocalDate.now(reloj.withZone(zona))),new CanalCorreo());
    }
}
