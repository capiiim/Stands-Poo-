package filey.notificaciones;

public interface CanalNotificacion {
    ResultadoEnvio enviar(String destino,String asunto,String mensaje);
}
