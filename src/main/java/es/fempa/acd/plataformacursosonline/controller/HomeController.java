package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CursoService cursoService;

    public HomeController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cursos", cursoService.listarCursos());
        return "index";
    }
}
