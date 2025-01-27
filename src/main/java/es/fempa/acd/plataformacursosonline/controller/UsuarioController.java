package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/editar")
    public String editarUsuario(@PathVariable Long id,
                                 @RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam Rol rol) {
        usuarioService.editarUsuario(id, username, email, rol);
        return "redirect:/usuarios";
    }

    @GetMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
        public String mostrarPerfil(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "/usuarios/perfil";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/perfil/editar")
    public String editarPerfil(Principal principal,
                              @RequestParam String username,
                              @RequestParam String email) {
        Usuario usuario = usuarioService.buscarPorUsername(principal.getName())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuarioService.editarUsuario(usuario.getId(), username, email, usuario.getRol());
        
        return "redirect:/usuarios/perfil";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "usuarios/acceso-denegado";
    }
    
}
