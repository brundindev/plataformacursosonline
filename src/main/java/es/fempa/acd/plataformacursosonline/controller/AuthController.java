package es.fempa.acd.plataformacursosonline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.fempa.acd.plataformacursosonline.service.CustomUserDetailsService.CustomUserDetails;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;

import java.security.Principal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Autenticación", description = "API para la gestión de autenticación y autorización")
@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Mostrar página de login")
    @ApiResponse(responseCode = "200", description = "Página de login mostrada correctamente")
    @GetMapping("/login")
    public String login(Model model, String error) {
        if (error != null) {
            if (!usuarioService.existsByUsername(error)) {
                model.addAttribute("errorMessage", "El usuario no existe");
            } else {
                model.addAttribute("errorMessage", "Contraseña incorrecta");
            }
        }
        return "login";
    }

    @Operation(summary = "Mostrar dashboard de administrador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard mostrado correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return "admin/dashboard";
    }
    
    
}
