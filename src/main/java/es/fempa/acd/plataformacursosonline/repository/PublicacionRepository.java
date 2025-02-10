package es.fempa.acd.plataformacursosonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.fempa.acd.plataformacursosonline.model.Publicacion;

/**
 * Repositorio para la entidad Publicacion.
 * Proporciona métodos para acceder y manipular los datos de las publicaciones en la base de datos.
 */
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    
    /**
     * Busca todas las publicaciones asociadas a un curso específico
     * @param cursoId ID del curso
     * @return Lista de publicaciones del curso
     */
    List<Publicacion> findByCursoId(Long cursoId);
}