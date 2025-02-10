package es.fempa.acd.plataformacursosonline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Esta clase es el controlador que maneja el registro de usuarios.
 * Las anotaciones indican:
 * @Tag - Para documentación Swagger
 * @Controller - Indica que es un controlador Spring
 * @RequestMapping - Todas las rutas empiezan con "/registro"
 */
@Tag(name = "Registro", description = "API para el registro de usuarios")
@Controller
@RequestMapping("/registro")
public class RegistroController {

    // Inyección de dependencias necesarias
    @Autowired
    private UsuarioService usuarioService;    // Servicio para operaciones con usuarios
    @Autowired
    private PasswordEncoder passwordEncoder;   // Para encriptar contraseñas

    /**
     * Muestra el formulario de registro
     * @param model Modelo para pasar datos a la vista
     * @return Vista del formulario de registro
     */
    @Operation(summary = "Mostrar formulario de registro")
    @ApiResponse(responseCode = "200", description = "Formulario mostrado correctamente")
    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("roles", Rol.values());  // Agrega los roles al modelo
        return "register";                          // Retorna la vista register.html
    }

    /**
     * Este método POST procesa el registro del usuario
     * Recibe los datos del formulario y realiza las validaciones
     */
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
            // Validación: verifica si el username ya existe
            if (usuarioService.existsByUsername(username)) {
                model.addAttribute("errorMessage", "El nombre de usuario ya está en uso");
                model.addAttribute("roles", Rol.values());
                return "register";
            }

            // Validación: verifica si el email ya existe
            if (usuarioService.existsByEmail(email)) {
                model.addAttribute("errorMessage", "El email ya está en uso");
                model.addAttribute("roles", Rol.values());
                return "register";
            }

            // Crear y configurar el nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));  // Encripta la contraseña
            usuario.setEmail(email);
            usuario.setRol(rol);

            // Guardar el usuario y redirigir al login
            usuarioService.registerUsuario(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "Registro exitoso. Por favor, inicie sesión.");
            return "redirect:/login";

        } catch (Exception e) {
            // Manejo de errores: vuelve al formulario con mensaje de error
            model.addAttribute("errorMessage", "Error en el registro: " + e.getMessage());
            model.addAttribute("roles", Rol.values());
            return "register";
        }
    }
}