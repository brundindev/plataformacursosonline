package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.service.CustomUserDetailsService;
import es.fempa.acd.plataformacursosonline.service.CustomUserDetailsService.CustomUserDetails;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CustomUserDetailsService customUserDetailsService;

    public UsuarioController(UsuarioService usuarioService, CustomUserDetailsService customUserDetailsService) {
        this.usuarioService = usuarioService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("roles", Rol.values());
        return "usuarios/nuevo";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    @PostMapping
    public String crearUsuario(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam Rol rol) {
        usuarioService.crearUsuario(username, password, email, rol);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null && usuario.getRol() == Rol.ADMIN) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede eliminar a un usuario administrador.");
            return "redirect:/usuarios";
        }
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return "error";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "usuarios/editar";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    @PostMapping("/{id}/editar")
    public String editarUsuario(@PathVariable Long id,
                           Principal principal,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam(required = false) String password,
                           @RequestParam Rol rol) {
        
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin && !userDetails.getId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para editar este perfil");
        }

        if (password != null && !password.isEmpty()) {
            usuarioService.editarUsuario(id, username, email, password, rol);
        } else {
            Usuario usuario = usuarioService.buscarPorId(id);
            usuarioService.editarUsuario(id, username, email, usuario.getPassword(), rol);
        }
        
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    @GetMapping("/perfil/editar/{id}")
    public String mostrarFormularioEditarPerfil(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        model.addAttribute("usuario", usuario);
        return "/usuarios/perfil";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR') or hasRole('ESTUDIANTE')")
    @PostMapping("/perfil/editar/{id}")
    public String editarPerfil(@PathVariable Long id,
                               Principal principal,
                               @RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        Long userId = userDetails.getId();

        if (!userId.equals(id)) {
            throw new AccessDeniedException("No tienes permiso para editar este perfil");
        }

        Usuario usuario = usuarioService.buscarPorId(userId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        usuarioService.editarUsuario(userId, username, email, password, usuario.getRol());

        Usuario usuarioActualizado = usuarioService.buscarPorId(userId);
        CustomUserDetails newUserDetails = new CustomUserDetails(usuarioActualizado);
        
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                newUserDetails, 
                null, 
                newUserDetails.getAuthorities()
            )
        );

        return "redirect:/home";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "usuarios/acceso-denegado";
    }
    
}
