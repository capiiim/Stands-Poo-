package filey.datos.memoria;

import filey.datos.Administrador;
import filey.datos.repositorios.AdministradorRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class AdministradorRepositoryMemoria implements AdministradorRepository {
    private final Map<Long,Administrador> datos=new ConcurrentHashMap<>();
    @Override public void guardar(Administrador a) {if (datos.putIfAbsent(a.id(),a)!=null) throw new IllegalArgumentException("Admin ya existe");}
    @Override public Optional<Administrador> buscarPorUsuario(String usuario) {
        return datos.values().stream().filter(a->a.usuario().equals(usuario)).findAny();
    }
    @Override public Optional<Administrador> buscarPorId(long id) {return Optional.ofNullable(datos.get(id));}
}
