package es.fempa.acd.plataformacursosonline.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Entidad que representa un curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del curso", example = "1")
    private Long id;

    @Schema(description = "Nombre del curso", example = "Programación Java")
    private String nombre;

    @Schema(description = "Descripción del curso", example = "Curso completo de Java")
    private String descripcion;

    @Schema(description = "Precio del curso", example = "99.99")
    private Double precio;

    public Curso(){
        
    }

    public Curso(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}