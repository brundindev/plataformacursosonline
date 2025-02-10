package es.fempa.acd.plataformacursosonline.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.service.CursoService;
import es.fempa.acd.plataformacursosonline.service.PublicacionService;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para la gestión de cursos.
 * Maneja todas las operaciones relacionadas con cursos incluyendo listado, creación y edición.
 */
@Tag(name = "Cursos", description = "API para gestionar cursos")
@Controller
@RequestMapping("/cursos")
public class CursosController {

    private final CursoService cursoService;
    private final PublicacionService publicacionService;
    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta los servicios necesarios
     */
    public CursosController(CursoService cursoService, PublicacionService publicacionService, UsuarioService usuarioService) {
        this.cursoService = cursoService;
        this.publicacionService = publicacionService;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener todos los cursos")
    @ApiResponse(responseCode = "200", description = "Lista de cursos encontrada")
    @GetMapping
    public String listarCursos(Model model, Principal principal) {
        List<Curso> cursos = cursoService.listarCursos();
        model.addAttribute("cursos", cursos);
        
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        model.addAttribute("inscripciones", cursos.stream()
            .collect(Collectors.toMap(Curso::getId, curso -> usuarioService.estaInscritoEnCurso(usuario.getId(), curso.getId()))));
        
        return "cursos/lista";
    }

    @Operation(summary = "Mostrar formulario de nuevo curso")
    @ApiResponse(responseCode = "200", description = "Formulario mostrado correctamente")
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevoCurso() {
        return "cursos/nuevo";
    }

    @Operation(summary = "Crear nuevo curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Curso creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del curso inválidos")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String crearCurso(@RequestParam String nombre,
                             @RequestParam String descripcion,
                             @RequestParam double precio) {
        cursoService.crearCurso(nombre, descripcion, precio);
        return "redirect:/cursos";
    }

    @Operation(summary = "Mostrar formulario de edición de curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulario mostrado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditarCurso(@PathVariable Long id, Model model) {
        Curso curso = cursoService.buscarPorId(id);
        model.addAttribute("curso", curso);
        return "cursos/editar";
    }

    @Operation(summary = "Editar curso existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @PostMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String editarCurso(@PathVariable Long id,
                              @RequestParam String nombre,
                              @RequestParam String descripcion,
                              @RequestParam double precio) {
        Curso curso = cursoService.buscarPorId(id);
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setPrecio(precio);
        cursoService.actualizarCurso(curso);
        return "redirect:/cursos";
    }

    @Operation(summary = "Eliminar curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso eliminado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return "redirect:/cursos";
    }

    @Operation(summary = "Ver detalles de un curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalles del curso mostrados correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String verCurso(@PathVariable Long id, Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!usuario.getRol().equals(Rol.ADMIN) && !usuarioService.estaInscritoEnCurso(usuario.getId(), id)) {
            return "error";
        }

        Curso curso = cursoService.buscarPorId(id);
        if (curso == null) {
            return "error";
        }
        model.addAttribute("curso", curso);
        model.addAttribute("publicaciones", publicacionService.listarPublicacionesPorCurso(id));
        return "cursos/ver";
    }

    @Operation(summary = "Unirse a un curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario inscrito correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @PostMapping("/{id}/unirse")
    @PreAuthorize("isAuthenticated()")
    public String unirseCurso(@PathVariable Long id, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuarioService.inscribirUsuarioEnCurso(usuario.getId(), id);
        return "redirect:/cursos";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/desapuntarse")
    public String desapuntarseCurso(@PathVariable Long id, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioService.desinscribirUsuarioDeCurso(usuario.getId(), id);
        return "redirect:/cursos";
    }
}