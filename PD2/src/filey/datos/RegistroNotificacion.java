package filey.datos;

import java.time.LocalDateTime;

public final class RegistroNotificacion {
    private final long id, editorialId;
    private final String tipoEvento, claveUnica, canal, destino, asunto, mensaje;
    private final LocalDateTime fechaHora;
    private String resultado;
    private int intentos;

    public RegistroNotificacion(long id, long editorialId, String tipoEvento, String claveUnica,
                                String canal, String destino, String asunto, String mensaje,
                                LocalDateTime fechaHora) {
        this.id=id; this.editorialId=editorialId; this.tipoEvento=tipoEvento;
        this.claveUnica=claveUnica; this.canal=canal; this.destino=destino;
        this.asunto=asunto; this.mensaje=mensaje; this.fechaHora=fechaHora;
        this.resultado="PENDIENTE"; this.intentos=0;
    }
    public RegistroNotificacion(RegistroNotificacion r) {
        this.id=r.id; editorialId=r.editorialId; tipoEvento=r.tipoEvento;
        claveUnica=r.claveUnica; canal=r.canal; destino=r.destino;
        asunto=r.asunto; mensaje=r.mensaje; fechaHora=r.fechaHora;
        resultado=r.resultado; intentos=r.intentos;
    }
    public long getId() {return id;}
    public long getEditorialId() {return editorialId;}
    public String getTipoEvento() {return tipoEvento;}
    public String getClaveUnica() {return claveUnica;}
    public String getCanal() {return canal;}
    public String getDestino() {return destino;}
    public String getAsunto() {return asunto;}
    public String getMensaje() {return mensaje;}
    public LocalDateTime getFechaHora() {return fechaHora;}
    public String getResultado() {return resultado;}
    public int getIntentos() {return intentos;}
    public void registrarResultado(boolean exito) { intentos++; resultado=exito?"ENVIADO":"PENDIENTE"; }
    @Override public String toString() {return "Notificación " + id + " " + tipoEvento + " " + resultado + " (intentos=" + intentos + ")";}
}
