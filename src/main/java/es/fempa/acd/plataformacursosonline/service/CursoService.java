package es.fempa.acd.plataformacursosonline.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.repository.CursoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Servicio que gestiona la lógica de negocio de los cursos.
 * Maneja todas las operaciones relacionadas con cursos como creación, edición, eliminación y búsqueda.
 */
@Service
@Tag(name = "Servicio de Cursos", description = "Lógica de negocio para cursos")
public class CursoService {
    private final CursoRepository cursoRepository;

    /**
     * Constructor del servicio
     * @param cursoRepository Repositorio de cursos inyectado
     */
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    /**
     * Lista todos los cursos disponibles
     * @return Lista de cursos
     */
    @Operation(summary = "Listar todos los cursos disponibles")
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    /**
     * Crea un nuevo curso
     * @throws IllegalArgumentException si el curso ya existe
     */
    @Operation(summary = "Crear un nuevo curso")
    public Curso crearCurso(String nombre, String descripcion, double precio) {
        if (cursoRepository.findByNombre(nombre) == null) {
            Curso curso = new Curso(nombre, descripcion, precio);
            cursoRepository.save(curso);
            return curso;
        } else {
            throw new IllegalArgumentException("El curso ya existe.");
        }
    }

    @Operation(summary = "Buscar curso por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso encontrado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    @Operation(summary = "Actualizar curso existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public void actualizarCurso(Curso curso) {
        cursoRepository.save(curso);
    }

    @Operation(summary = "Eliminar curso por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public void eliminarCurso(Long id) {
        cursoRepository.deleteById(id);
    }
}