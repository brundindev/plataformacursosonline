package es.fempa.acd.plataformacursosonline.repository;

import es.fempa.acd.plataformacursosonline.model.Favorito;
import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // Obtener los favoritos de un usuario
    List<Favorito> findByUsuario(Usuario usuario);

    // Verificar si un producto ya está en los favoritos de un usuario
    Optional<Favorito> findByUsuarioAndProducto(Usuario usuario, Curso producto);
}
