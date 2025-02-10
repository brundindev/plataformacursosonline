package es.fempa.acd.plataformacursosonline.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.fempa.acd.plataformacursosonline.model.Publicacion;
import es.fempa.acd.plataformacursosonline.repository.PublicacionRepository;

/**
 * Servicio para gestionar las publicaciones.
 * Implementa la lógica de negocio para las operaciones con publicaciones.
 */
@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    /**
     * Constructor del servicio
     * @param publicacionRepository Repositorio de publicaciones inyectado
     */
    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    /**
     * Crea una nueva publicación para un curso.
     * Valida el tamaño del archivo y guarda la publicación con su documento adjunto.
     */
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

    /**
     * Obtiene todas las publicaciones asociadas a un curso específico.
     */
    public List<Publicacion> listarPublicacionesPorCurso(Long cursoId) {
        return publicacionRepository.findByCursoId(cursoId);
    }

    /**
     * Busca una publicación por su ID
     * @param id ID de la publicación
     * @return Publicación encontrada o null si no existe
     */
    public Publicacion buscarPorId(Long id) {
        return publicacionRepository.findById(id).orElse(null);
    }

    /**
     * Edita una publicación existente
     * @param id ID de la publicación a editar
     * @param titulo Nuevo título
     * @param descripcion Nueva descripción
     * @param documento Nuevo documento adjunto (opcional)
     * @throws IllegalArgumentException si la publicación no existe
     */
    public void editarPublicacion(Long id, String titulo, String descripcion, MultipartFile documento) {
        Publicacion publicacion = buscarPorId(id);
        if (publicacion == null) {
            throw new IllegalArgumentException("Publicación no encontrada");
        }
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        
        if (documento != null && !documento.isEmpty()) {
            if (documento.getSize() > 1_000_000) {
                throw new IllegalArgumentException("El tamaño del archivo no debe exceder 1MB");
            }
            try {
                byte[] contenido = documento.getBytes();
                publicacion.setDocumento(contenido);
                publicacion.setNombreArchivo(documento.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar el archivo", e);
            }
        }
        
        publicacionRepository.save(publicacion);
    }
}
