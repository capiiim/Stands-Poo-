package filey.negocio;

import java.math.BigDecimal;

public record Resumen(long standsDisponibles,long standsReservados,long standsOcupados,long standsBloqueados,
                      long reservasVigentes,long reservasVencidas,long reservasCanceladas,
                      BigDecimal totalFacturadoBase,BigDecimal totalCobrado,BigDecimal saldoPendiente) {
    @Override public String toString() {
        return "Stands [disponibles="+standsDisponibles+", reservados="+standsReservados+
             ", ocupados="+standsOcupados+", bloqueados="+standsBloqueados+"]\n"+
             "Reservas [vigentes="+reservasVigentes+", vencidas="+reservasVencidas+
             ", canceladas="+reservasCanceladas+"]\n"+
             "Importes [base=$"+totalFacturadoBase+", cobrado=$"+totalCobrado+
             ", pendiente=$"+saldoPendiente+"]";
    }
}
