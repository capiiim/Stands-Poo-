package filey.negocio;

import filey.datos.Administrador;
import filey.datos.repositorios.AdministradorRepository;
import filey.negocio.excepciones.AutorizacionException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class AutorizacionService {
    private final AdministradorRepository administradores;
    private final Map<String,Long> sesiones=new ConcurrentHashMap<>();
    public AutorizacionService(AdministradorRepository administradores) {this.administradores=administradores;}
    public void crearAdministradorInicial(long id,String usuario,char[] clave) {
        if (administradores.buscarPorUsuario(usuario).isPresent()) throw new IllegalStateException("Usuario ya existe");
        administradores.guardar(new Administrador(id,usuario,SeguridadContrasena.hash(clave),"ADMIN"));
    }
    public String iniciarSesion(String usuario,char[] clave) {
        Administrador admin=administradores.buscarPorUsuario(usuario)
                .orElseThrow(()->new AutorizacionException("Credenciales incorrectas"));
        if (!SeguridadContrasena.verificar(clave,admin.hashContrasena()))
            throw new AutorizacionException("Credenciales incorrectas");
        String token=UUID.randomUUID().toString();
        sesiones.put(token,admin.id()); return token;
    }
    public String exigirAdministrador(String token) {
        Long id=sesiones.get(token);
        if (id==null) throw new AutorizacionException("Debes autenticarte como administrador");
        Administrador admin=administradores.buscarPorId(id)
                .orElseThrow(()->new AutorizacionException("Cuenta desconocida"));
        if (!"ADMIN".equals(admin.rol())) throw new AutorizacionException("Permiso de administrador requerido");
        return admin.usuario();
    }
    public void cerrarSesion(String token) {sesiones.remove(token);}
}
