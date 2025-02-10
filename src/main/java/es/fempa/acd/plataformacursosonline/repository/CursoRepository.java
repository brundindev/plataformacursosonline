package es.fempa.acd.plataformacursosonline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fempa.acd.plataformacursosonline.model.Curso;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Repositorio para la entidad Curso.
 * Proporciona métodos para acceder y manipular los datos de cursos en la base de datos.
 */
@Repository
@Tag(name = "Repositorio de Cursos", description = "Operaciones de base de datos para cursos")
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    /**
     * Busca un curso por su nombre
     * @param nombre Nombre del curso a buscar
     * @return Curso encontrado o null
     */
    @Operation(summary = "Buscar curso por nombre")
    @ApiResponse(responseCode = "200", description = "Curso encontrado")
    Curso findByNombre(String nombre);

    /**
     * Busca cursos por precio menor o igual al especificado
     * @param precio Precio máximo a buscar
     * @return Lista de cursos que cumplen el criterio
     */
    List<Curso> findByPrecioLessThanEqual(double precio);
}