package es.fempa.acd.plataformacursosonline.repository;

import es.fempa.acd.plataformacursosonline.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Repository
@Tag(name = "Repositorio de Cursos", description = "Operaciones de base de datos para cursos")
public interface CursoRepository extends JpaRepository<Curso, Long> {
    @Operation(summary = "Buscar curso por nombre")
    @ApiResponse(responseCode = "200", description = "Curso encontrado")
    Curso findByNombre(String nombre);
}