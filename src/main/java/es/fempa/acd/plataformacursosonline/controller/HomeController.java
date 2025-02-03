package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.service.CursoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Home", description = "API para la página principal")
@Controller
public class HomeController {

    private final CursoService cursoService;

    public HomeController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @Operation(summary = "Mostrar página principal")
    @ApiResponse(responseCode = "200", description = "Página principal cargada correctamente")
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("cursos", cursoService.listarCursos());
        return "home";
    }
}
