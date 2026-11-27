package filey.pd5;

import filey.pd5.bd.ConexionBD;
import filey.pd5.controlador.CrudController;
import filey.pd5.vista.ConsolaVista;

public class Main {
    public static void main(String[] args) {
        if (!new CrudController(new ConexionBD(), new ConsolaVista()).ejecutar()) {
            System.exit(1);
        }
    }
}
