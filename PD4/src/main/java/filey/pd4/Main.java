package filey.pd4;
import filey.pd4.bd.ConexionBD;import filey.pd4.controlador.ConsultaController;import filey.pd4.vista.ConsolaVista;
public class Main { public static void main(String[] args){new ConsultaController(new ConexionBD(),new ConsolaVista()).ejecutar();} }
