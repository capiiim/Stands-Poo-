package filey;

import filey.datos.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.Scanner;

/** Consola administrativa de ejemplo. Datos solo persisten durante la ejecución. */
public final class Main {
    private static final Scanner ENTRADA=new Scanner(System.in);
    public static void main(String[] args) {
        SistemaFiley app=SistemaFiley.crearDemostracion();
        app.autorizacion.crearAdministradorInicial(1,"admin","Filey2026!".toCharArray());
        String token=app.autorizacion.iniciarSesion("admin","Filey2026!".toCharArray());
        app.stands.crearStand(token,new BigDecimal("10"),new BigDecimal("2000"));
        app.stands.crearStand(token,new BigDecimal("12"),new BigDecimal("1500"));
        app.editoriales.registrar(token,"Editorial Demo","editorial@ejemplo.test");
        if (args.length>0 && args[0].equals("--demo")) {demostracion(app,token);return;}
        System.out.println("FILEY | Backend Java (consola de demostración)");
        System.out.println("Inicia con 2 stands y la Editorial Demo. Correo SIMULADO, sin envíos reales.");
        boolean seguir=true;
        while (seguir) {
            System.out.println("\n1 Ver stands     2 Registrar editorial   3 Crear stand      4 Reservar");
            System.out.println("5 Registrar pago 6 Cancelar reserva      7 Bloquear stand    8 Liberar stand");
            System.out.println("9 Ver reservas  10 Ver resumen           11 Revisar avisos   12 Ver historial");
            System.out.println("13 Configurar    14 Modificar stand       15 Reintentos      16 Ver notificaciones");
            System.out.println("17 Ver editoriales                  0 Salir");
            try {
                switch (leer("Opción")) {
                    case "1" -> app.consultas.stands(token).forEach(System.out::println);
                    case "2" -> System.out.println(app.editoriales.registrar(token,leer("Nombre"),leer("Correo")));
                    case "3" -> System.out.println(app.stands.crearStand(token,decimal("m2"),decimal("Precio por m2")));
                    case "4" -> System.out.println(app.reservas.reservar(token,entero("ID stand"),entero("ID editorial")));
                    case "5" -> {
                        long reserva=entero("ID reserva");
                        System.out.println(app.pagos.registrarPago(token,reserva,decimal("Monto")));
                        System.out.println("Saldo: $"+app.pagos.calcularSaldo(token,reserva));
                    }
                    case "6" -> {app.reservas.cancelar(token,entero("ID reserva"));System.out.println("Reserva cancelada");}
                    case "7" -> {app.stands.bloquear(token,entero("ID stand"));System.out.println("Stand bloqueado");}
                    case "8" -> {app.stands.liberarBloqueado(token,entero("ID stand"));System.out.println("Stand disponible");}
                    case "9" -> app.consultas.reservas(token).forEach(System.out::println);
                    case "10" -> System.out.println(app.resumenes.resumenStands(token));
                    case "11" -> {
                        System.out.println("Vencidas: "+app.vencimientos.procesarReservasVencidas());
                        System.out.println("Avisos evaluados: "+app.vencimientos.procesarAvisos());
                    }
                    case "12" -> app.consultas.historial(token).forEach(System.out::println);
                    case "13" -> configurar(app,token);
                    case "14" -> System.out.println(app.stands.modificarStand(token,entero("ID stand"),decimal("m2"),decimal("Precio por m2")));
                    case "15" -> System.out.println("Reintentos: "+app.reintentos.reintentarPendientes());
                    case "16" -> app.consultas.notificaciones(token).forEach(System.out::println);
                    case "17" -> app.consultas.editoriales(token).forEach(System.out::println);
                    case "0" -> seguir=false;
                    default -> System.out.println("Opción desconocida");
                }
            } catch (RuntimeException ex) {System.out.println("ERROR: "+ex.getMessage());}
        }
        app.autorizacion.cerrarSesion(token);
        System.out.println("Sesión finalizada. Los datos eran solo en memoria.");
    }
    private static void demostracion(SistemaFiley app,String token) {
        System.out.println("\n--- DEMOSTRACIÓN INTEGRAL FILEY ---");
        var reserva=app.reservas.reservar(token,1,1);
        System.out.println("Creada: "+reserva);
        try {app.reservas.reservar(token,1,1);}
        catch (IllegalStateException ex) {System.out.println("Doble reserva evitada: "+ex.getMessage());}
        app.pagos.registrarPago(token,reserva.getId(),new BigDecimal("3000"));
        System.out.println("Porcentaje cubierto: "+app.pagos.porcentajeCubierto(token,reserva.getId()));
        System.out.println("Estado stand 1: "+app.consultas.stands(token).get(0).getEstado());
        System.out.println("Saldo: $"+app.pagos.calcularSaldo(token,reserva.getId()));
        System.out.println("Avisos evaluados: "+app.vencimientos.procesarAvisos());
        System.out.println(app.resumenes.resumenCobros(token));
        System.out.println("Historial: "+app.consultas.historial(token).size()+" cambios");
        System.out.println("Notificaciones: "+app.consultas.notificaciones(token).size()+" registros");
        System.out.println("--- FIN DEMOSTRACIÓN ---");
    }
    private static void configurar(SistemaFiley app,String token) {
        Configuracion actual=app.configuracion.obtenerParametros(token);
        System.out.println("Actual: "+actual);
        System.out.println("Deja vacío para mantener cada valor.");
        LocalDate reserva=fechaOValor("Límite para reservar (AAAA-MM-DD)",actual.getFechaLimiteReserva());
        LocalDate descuento=fechaOValor("Límite de descuento (AAAA-MM-DD)",actual.getFechaDescuento());
        LocalDate pago=fechaOValor("Límite pago total (AAAA-MM-DD)",actual.getFechaPagoTotal());
        Duration plazo=Duration.ofHours(enteroOValor("Duración de reserva en horas",actual.getPlazoReserva().toHours()));
        BigDecimal minimo=decimalOValor("Mínimo para ocupar (0.15 = 15%)",actual.getPorcentajeMinimo());
        BigDecimal rebaja=decimalOValor("Descuento (0.05 = 5%)",actual.getPorcentajeDescuento());
        Duration aviso=Duration.ofHours(enteroOValor("Anticipación de avisos en horas",actual.getAnticipacionAviso().toHours()));
        String zona=leer("Zona horaria ["+actual.getZonaHoraria()+"] (vacío=mantener)");
        ZoneId zone=zona.isBlank()?actual.getZonaHoraria():ZoneId.of(zona);
        app.configuracion.actualizarParametros(token,new Configuracion(reserva,descuento,pago,plazo,minimo,rebaja,aviso,zone));
        System.out.println("Configuración actualizada");
    }
    private static String leer(String etiqueta) {System.out.print(etiqueta+": ");return ENTRADA.nextLine().trim();}
    private static long entero(String etiqueta) {return Long.parseLong(leer(etiqueta));}
    private static BigDecimal decimal(String etiqueta) {return new BigDecimal(leer(etiqueta));}
    private static long enteroOValor(String etiqueta,long actual) {
        String s=leer(etiqueta+" ["+actual+"]");return s.isBlank()?actual:Long.parseLong(s);
    }
    private static BigDecimal decimalOValor(String etiqueta,BigDecimal actual) {
        String s=leer(etiqueta+" ["+actual+"]");return s.isBlank()?actual:new BigDecimal(s);
    }
    private static LocalDate fechaOValor(String etiqueta,LocalDate actual) {
        String s=leer(etiqueta+" ["+actual+"]");return s.isBlank()?actual:LocalDate.parse(s);
    }
    private Main() {}
}
