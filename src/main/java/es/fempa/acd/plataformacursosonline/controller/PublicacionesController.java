package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.Publicacion;
import es.fempa.acd.plataformacursosonline.service.PublicacionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.prepost.PreAuthorize;

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

    @GetMapping("/download")
    public ResponseEntity<byte[]> descargarPublicacion(@RequestParam Long id) {
        Publicacion publicacion = publicacionService.buscarPorId(id);
        if (publicacion == null || publicacion.getDocumento() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + publicacion.getNombreArchivo() + "\"")
                .body(publicacion.getDocumento());
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    public String mostrarFormularioEditarPublicacion(@PathVariable Long id, Model model) {
        Publicacion publicacion = publicacionService.buscarPorId(id);
        if (publicacion == null) {
            return "error";
        }
        model.addAttribute("publicacion", publicacion);
        return "publicaciones/editar";
    }

    @PostMapping("/{id}/editar")
    public String editarPublicacion(@PathVariable Long id,
                                 @RequestParam String titulo,
                                 @RequestParam String descripcion,
                                 @RequestParam(required = false) MultipartFile documento) {
        try {
            publicacionService.editarPublicacion(id, titulo, descripcion, documento);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/cursos/" + publicacionService.buscarPorId(id).getCursoId();
    }
}