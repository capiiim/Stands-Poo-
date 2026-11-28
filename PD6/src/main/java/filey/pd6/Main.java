package filey.pd6;

import filey.pd6.bd.ConexionBD;
import filey.pd6.controlador.CrudController;
import filey.pd6.vista.ConsolaVista;

public class Main {
    public static void main(String[] args) {
        if (!new CrudController(new ConexionBD(), new ConsolaVista()).ejecutar()) {
            System.exit(1);
        }
    }
}
