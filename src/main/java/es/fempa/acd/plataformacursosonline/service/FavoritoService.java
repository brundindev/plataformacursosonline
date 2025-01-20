package es.fempa.acd.plataformacursosonline.service;

import es.fempa.acd.plataformacursosonline.model.Favorito;
import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.repository.FavoritoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    // Agregar un producto a los favoritos de un usuario
    public Favorito agregarFavorito(Usuario usuario, Curso producto) {
        if (favoritoRepository.findByUsuarioAndProducto(usuario, producto).isPresent()) {
            throw new IllegalArgumentException("El producto ya está en favoritos");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setProducto(producto);

        return favoritoRepository.save(favorito);
    }

    // Listar los favoritos de un usuario
    public List<Favorito> listarFavoritosPorUsuario(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario);
    }

    // Eliminar un favorito
    public void eliminarFavorito(Long favoritoId) {
        favoritoRepository.deleteById(favoritoId);
    }
}
