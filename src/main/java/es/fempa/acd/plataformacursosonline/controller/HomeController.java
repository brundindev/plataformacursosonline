package es.fempa.acd.plataformacursosonline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("title", "Bienvenido a la web de FEMPA");
        return "index";
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("title", "Bienvenido a la sección de contacto");
        return "contacto";
    }

}
