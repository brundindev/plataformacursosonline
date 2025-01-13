package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.Inmueble;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ServiciosController {



    @GetMapping("/servicios")
    public String listarServicios(Model model) {
        List<Inmueble> servicios = List.of(
                new Inmueble("Servicio 1", 1000.0),
                new Inmueble("Servicio 2", 1500.0),
                new Inmueble("Servicio 3", 2000.0)
        );
        model.addAttribute("servicios", servicios);

        model.addAttribute("title", "Bienvenido a la lista de Servicios");

        return "servicios";
    }
}
