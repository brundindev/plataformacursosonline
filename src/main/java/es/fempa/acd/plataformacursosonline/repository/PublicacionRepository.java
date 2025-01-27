package es.fempa.acd.plataformacursosonline.repository;

import es.fempa.acd.plataformacursosonline.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByCursoId(Long cursoId);
}