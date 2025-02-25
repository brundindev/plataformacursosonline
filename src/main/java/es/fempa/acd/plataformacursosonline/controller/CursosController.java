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
 * Controlador para la gestión de cursos en la plataforma.
 * Proporciona endpoints para listar, crear, editar, eliminar y gestionar inscripciones en cursos.
 */
@Tag(name = "Cursos", description = "API para gestionar cursos")
@Controller
@RequestMapping("/cursos")
public class CursosController {

    // Servicios necesarios para la lógica de negocio
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

    /**
     * Lista todos los cursos disponibles y marca si el usuario actual está inscrito en cada uno.
     * @param model Modelo para pasar datos a la vista
     * @param principal Usuario autenticado actual
     * @return Vista de lista de cursos
     */
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

    /**
     * Muestra el formulario para crear un nuevo curso.
     * Solo accesible para administradores.
     */
    @Operation(summary = "Mostrar formulario de nuevo curso")
    @ApiResponse(responseCode = "200", description = "Formulario mostrado correctamente")
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevoCurso() {
        return "cursos/nuevo";
    }

    /**
     * Procesa la creación de un nuevo curso.
     * Solo accesible para administradores.
     * @param nombre Nombre del curso
     * @param descripcion Descripción del curso
     * @param precio Precio del curso
     */
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

    /**
     * Muestra el formulario para editar un curso existente.
     * Solo accesible para administradores.
     * @param id ID del curso a editar
     */
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

    /**
     * Procesa la actualización de un curso existente.
     * Solo accesible para administradores.
     */
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

    /**
     * Elimina un curso específico.
     * Solo accesible para administradores.
     * @param id ID del curso a eliminar
     */
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

    /**
     * Muestra los detalles de un curso específico.
     * Solo accesible para usuarios inscritos o administradores.
     * @param id ID del curso a visualizar
     * @param principal Usuario autenticado actual
     */
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
            return "redirect:/cursos";
        }

        Curso curso = cursoService.buscarPorId(id);
        if (curso == null) {
            return "redirect:/cursos";
        }
        
        model.addAttribute("curso", curso);
        model.addAttribute("publicaciones", publicacionService.listarPublicacionesPorCurso(id));
        return "cursos/ver";
    }

    /**
     * Procesa la inscripción de un usuario en un curso.
     * @param id ID del curso
     * @param principal Usuario que se inscribe
     */
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

    /**
     * Procesa la desinscripción de un usuario de un curso.
     * @param id ID del curso
     * @param principal Usuario que se desapunta
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/desapuntarse")
    public String desapuntarseCurso(@PathVariable Long id, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioService.desinscribirUsuarioDeCurso(usuario.getId(), id);
        return "redirect:/cursos";
    }

    /**
     * Muestra la página de pago para un curso.
     * Solo accesible para usuarios autenticados.
     * @param id ID del curso
     * @param model Modelo para pasar datos a la vista
     * @param principal Usuario autenticado actual
     */
    @Operation(summary = "Mostrar página de pago para un curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de pago mostrada correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/{id}/pago")
    @PreAuthorize("isAuthenticated()")
    public String mostrarPaginaPago(@PathVariable Long id, Model model, Principal principal) {
        try {
            Curso curso = cursoService.buscarPorId(id);
            if (curso == null) {
                return "redirect:/error";
            }
            
            Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            if (usuarioService.estaInscritoEnCurso(usuario.getId(), id)) {
                return "redirect:/cursos";
            }
            
            model.addAttribute("curso", curso);
            return "cursos/pago";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    /**
     * Procesa el pago y la inscripción de un usuario en un curso.
     * Solo accesible para usuarios autenticados.
     * @param id ID del curso al que se quiere inscribir
     * @param principal Usuario que realiza el pago
     * @return Redirección a la página del curso si el pago es exitoso, o a error si falla
     */
    @Operation(summary = "Procesar pago e inscripción en un curso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago procesado e inscripción realizada correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @PostMapping("/{id}/procesar-pago")
    @PreAuthorize("isAuthenticated()")
    public String procesarPago(@PathVariable Long id, Principal principal) {
        try {
            Curso curso = cursoService.buscarPorId(id);
            if (curso == null) {
                return "redirect:/error";
            }
            
            Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            if (usuarioService.estaInscritoEnCurso(usuario.getId(), id)) {
                return "redirect:/cursos";
            }
            
            usuarioService.inscribirUsuarioEnCurso(usuario.getId(), id);
            
            return "redirect:/cursos/" + id;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
}