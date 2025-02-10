package es.fempa.acd.plataformacursosonline.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.fempa.acd.plataformacursosonline.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para la página principal.
 * Maneja las peticiones a la página de inicio y dashboard.
 */
@Tag(name = "Home", description = "API para la página principal")
@Controller
public class HomeController {

    private final CursoService cursoService;

    public HomeController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    /**
     * Muestra la página de inicio
     * @return Vista de la página principal
     */
    @Operation(summary = "Mostrar página principal")
    @ApiResponse(responseCode = "200", description = "Página principal cargada correctamente")
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("cursos", cursoService.listarCursos());
        return "home";
    }

    /**
     * Muestra el dashboard según el rol del usuario
     * @param principal Usuario autenticado
     * @return Vista del dashboard correspondiente
     */
    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String dashboard(Principal principal) {
        // Implementación del dashboard
        return "dashboard";
    }
}
