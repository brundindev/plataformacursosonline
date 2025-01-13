package es.fempa.acd.plataformacursosonline.controller;

import es.fempa.acd.plataformacursosonline.model.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.fempa.acd.plataformacursosonline.model.Producto;

import java.util.List;

@Controller
public class ProductosController {

    ProductoService productoService;

    public ProductosController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        List<Producto> listaProductos = productoService.getProductos();
        model.addAttribute("prods", listaProductos);

        model.addAttribute("title", "Bienvenido a la lista de productos de FEMPA");

        return "productos";
    }
}
