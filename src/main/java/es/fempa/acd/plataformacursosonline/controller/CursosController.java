package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.service.CursoService;
import es.fempa.acd.plataformacursosonline.service.PublicacionService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cursos")
public class CursosController {

    private final CursoService cursoService;
    private final PublicacionService publicacionService;

    public CursosController(CursoService cursoService, PublicacionService publicacionService) {
        this.cursoService = cursoService;
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoService.listarCursos();
        model.addAttribute("cursos", cursos);
        return "cursos/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioNuevoCurso() {
        return "cursos/nuevo";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String crearCurso(@RequestParam String nombre,
                             @RequestParam String descripcion,
                             @RequestParam double precio) {
        cursoService.crearCurso(nombre, descripcion, precio);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String mostrarFormularioEditarCurso(@PathVariable Long id, Model model) {
        Curso curso = cursoService.buscarPorId(id);
        model.addAttribute("curso", curso);
        return "cursos/editar";
    }

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

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}")
    public String verCurso(@PathVariable Long id, Model model) {
        Curso curso = cursoService.buscarPorId(id);
        if (curso == null) {
            return "error";
        }
        model.addAttribute("curso", curso);
        model.addAttribute("publicaciones", publicacionService.listarPublicacionesPorCurso(id));
        return "cursos/ver";
    }
}