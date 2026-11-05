package filey.notificaciones;

// Canal de demostración: imprime mensajes. No manda correo real ni requiere SMTP.
public final class CanalCorreo implements CanalNotificacion {
    @Override public ResultadoEnvio enviar(String destino,String asunto,String mensaje) {
        if (destino==null || !destino.contains("@")) return new ResultadoEnvio(false,"Correo inválido");
        System.out.println("[CORREO SIMULADO] Para: "+destino+" | "+asunto+" | "+mensaje);
        return new ResultadoEnvio(true,"Simulación local: mensaje mostrado en consola");
    }
}
