package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "Registro", description = "API para el registro de usuarios")
@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Mostrar formulario de registro")
    @ApiResponse(responseCode = "200", description = "Formulario mostrado correctamente")
    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("roles", Rol.values());
        return "register";
    }

    @Operation(summary = "Registrar nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    })
    @PostMapping
    public String registrarUsuario(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   @RequestParam Rol rol,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (usuarioService.existsByUsername(username)) {
                model.addAttribute("errorMessage", "El nombre de usuario ya está en uso");
                model.addAttribute("roles", Rol.values());
                return "register";
            }
            if (usuarioService.existsByEmail(email)) {
                model.addAttribute("errorMessage", "El email ya está en uso");
                model.addAttribute("roles", Rol.values());
                return "register";
            }

            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setEmail(email);
            usuario.setRol(rol);

            usuarioService.registerUsuario(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Registro exitoso. Por favor, inicie sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error en el registro: " + e.getMessage());
            model.addAttribute("roles", Rol.values());
            return "register";
        }
    }
}