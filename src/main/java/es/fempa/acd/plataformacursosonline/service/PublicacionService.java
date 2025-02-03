package es.fempa.acd.plataformacursosonline.service;

import es.fempa.acd.plataformacursosonline.model.Publicacion;
import es.fempa.acd.plataformacursosonline.repository.PublicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    public void crearPublicacion(Long cursoId, String titulo, String descripcion, MultipartFile documento) {
        if (documento.getSize() > 1_000_000) {
            throw new IllegalArgumentException("El tamaño del archivo no debe exceder 1MB");
        }
    
        Publicacion publicacion = new Publicacion();
        publicacion.setCursoId(cursoId);
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
    
        try {
            byte[] contenido = documento.getBytes();
            publicacion.setDocumento(contenido);
            publicacion.setNombreArchivo(documento.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo", e);
        }
    
        publicacionRepository.save(publicacion);
    }

    public List<Publicacion> listarPublicacionesPorCurso(Long cursoId) {
        return publicacionRepository.findByCursoId(cursoId);
    }

    public Publicacion buscarPorId(Long id) {
        return publicacionRepository.findById(id).orElse(null);
    }
}
