package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.fempa.acd.plataformacursosonline.model.Curso;

import java.util.List;

@Controller
public class ProductosController {

    CursoService cursoService;

    public ProductosController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model) {
        List<Curso> listaCursos = cursoService.getCursos();
        model.addAttribute("cursos", listaCursos);

        model.addAttribute("title", "Bienvenido a la lista de cursos de la Plataforma");

        return "cursos";
    }
}
