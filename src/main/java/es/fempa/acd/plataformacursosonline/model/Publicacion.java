package es.fempa.acd.plataformacursosonline.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

/**
 * Entidad que representa una publicación dentro de un curso.
 * Almacena información sobre documentos y materiales compartidos en los cursos.
 */
@Entity
public class Publicacion {
    
    /**
     * Identificador único de la publicación
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID del curso al que pertenece esta publicación
     */
    private Long cursoId;

    /**
     * Título de la publicación
     */
    private String titulo;

    /**
     * Descripción detallada de la publicación
     */
    private String descripcion;

    /**
     * Documento adjunto en formato de bytes
     */
    @Lob
    private byte[] documento;

    /**
     * Nombre original del archivo subido
     */
    private String nombreArchivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}