package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.service.PublicacionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionesController {

    private final PublicacionService publicacionService;

    public PublicacionesController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaPublicacion(@RequestParam Long cursoId, Model model) {
        model.addAttribute("cursoId", cursoId);
        return "publicaciones/nueva";
    }

    @PostMapping
    public String crearPublicacion(@RequestParam Long cursoId,
                                    @RequestParam String titulo,
                                    @RequestParam String descripcion,
                                    @RequestParam MultipartFile documento) {
        if (titulo.isEmpty() || descripcion.isEmpty()) {
            return "publicaciones/nueva";
        }
        
        try {
            publicacionService.crearPublicacion(cursoId, titulo, descripcion, documento);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        
        return "redirect:/cursos/" + cursoId;
    }
}