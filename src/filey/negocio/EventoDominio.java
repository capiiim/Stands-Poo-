package filey.negocio;

import java.time.LocalDateTime;

public interface EventoDominio {
    String tipo();
    LocalDateTime fechaHora();
    long editorialId();
    String detalle();
    String claveUnica();
}
