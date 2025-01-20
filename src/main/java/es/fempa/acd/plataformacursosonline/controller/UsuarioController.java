package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')") // Solo los administradores y profesores pueden listar usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista"; // Apunta a una plantilla Thymeleaf
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear nuevos usuarios
    @GetMapping("/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("roles", Rol.values());
        return "usuarios/nuevo"; // Apunta a una plantilla Thymeleaf
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')") // Solo los administradores y profesores pueden crear nuevos usuarios
    @PostMapping
    public String crearUsuario(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam Rol rol) {
        usuarioService.crearUsuario(username, password, email, rol);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden eliminar usuarios
    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden editar usuarios
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "usuarios/editar"; // Devuelve la plantilla "editar.html"
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden guardar cambios en usuarios
    @PostMapping("/{id}/editar")
    public String editarUsuario(@PathVariable Long id,
                            @RequestParam String username,
                            @RequestParam String email,
                            @RequestParam Rol rol) {
    usuarioService.editarUsuario(id, username, email, rol);
    return "redirect:/usuarios";
}
}
