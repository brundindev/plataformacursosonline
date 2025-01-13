package es.fempa.acd.plataformacursosonline.model;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    ProductoRepository productoRepository;

    public ProductoService( ProductoRepository productoRepository ){
        this.productoRepository = productoRepository;
    }

    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

}
