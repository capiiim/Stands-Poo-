package filey.negocio;

import filey.datos.Editorial;
import filey.datos.repositorios.EditorialRepository;
import java.util.List;

public final class EditorialService {
    private final EditorialRepository editoriales;
    private final AutorizacionService acceso;
    private final IdGenerador ids;
    public EditorialService(EditorialRepository editoriales,AutorizacionService acceso,IdGenerador ids) {
        this.editoriales=editoriales;this.acceso=acceso;this.ids=ids;
    }
    public Editorial registrar(String token,String nombre,String correo) {
        acceso.exigirAdministrador(token);
        return editoriales.guardar(new Editorial(ids.siguiente(),nombre,correo));
    }
    public List<Editorial> listar(String token) {acceso.exigirAdministrador(token);return editoriales.listarTodos();}
}
