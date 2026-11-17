package filey.notificaciones;

import filey.datos.*;
import filey.datos.repositorios.*;
import filey.negocio.*;

public final class ObservadorEmail implements ObservadorEvento {
    private final EditorialRepository editoriales;
    private final NotificacionRepository registros;
    private final CanalNotificacion canal;
    private final IdGenerador ids;
    private final Fechas fechas;
    public ObservadorEmail(EditorialRepository editoriales,NotificacionRepository registros,
                          CanalNotificacion canal,IdGenerador ids,Fechas fechas) {
        this.editoriales=editoriales;this.registros=registros;this.canal=canal;this.ids=ids;this.fechas=fechas;
    }
    @Override public void alRecibir(EventoDominio evento) {
        Editorial editorial=editoriales.buscarPorId(evento.editorialId()).orElseThrow();
        String clave=evento.claveUnica()+":CORREO";
        RegistroNotificacion registro=new RegistroNotificacion(ids.siguiente(),editorial.id(),evento.tipo(),
                clave,"CORREO",editorial.correo(),"FILEY: "+evento.tipo(),evento.detalle(),fechas.ahora());
        if (!registros.anexarSiNoExiste(registro)) return;
        intentar(registro,canal,registros);
    }
    static void intentar(RegistroNotificacion registro,CanalNotificacion canal,NotificacionRepository repositorio) {
        boolean enviado;
        try {enviado=canal.enviar(registro.getDestino(),registro.getAsunto(),registro.getMensaje()).exitoso();}
        catch (RuntimeException ex) {enviado=false;}
        registro.registrarResultado(enviado);
        repositorio.actualizar(registro);
    }
}
