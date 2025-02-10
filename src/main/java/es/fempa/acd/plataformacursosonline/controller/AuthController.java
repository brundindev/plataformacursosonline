package es.fempa.acd.plataformacursosonline.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.fempa.acd.plataformacursosonline.service.CustomUserDetailsService.CustomUserDetails;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para gestionar la autenticación de usuarios.
 * Maneja los procesos de login, logout y autorización.
 */
@Tag(name = "Autenticación", description = "API para la gestión de autenticación y autorización")
@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Muestra la página de login
     * @param error Mensaje de error si existe
     * @param model Modelo para pasar datos a la vista
     * @return Vista de login
     */
    @Operation(summary = "Mostrar página de login")
    @ApiResponse(responseCode = "200", description = "Página de login mostrada correctamente")
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            if (!usuarioService.existsByUsername(error)) {
                model.addAttribute("errorMessage", "El usuario no existe");
            } else {
                model.addAttribute("errorMessage", "Contraseña incorrecta");
            }
        }
        return "login";
    }

    /**
     * Muestra el dashboard del administrador
     * @param principal Usuario autenticado
     * @return Vista del dashboard
     */
    @Operation(summary = "Mostrar dashboard de administrador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard mostrado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminDashboard(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return "admin/dashboard";
    }
    
    
}
