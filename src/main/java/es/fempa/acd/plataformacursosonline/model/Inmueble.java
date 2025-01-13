package es.fempa.acd.plataformacursosonline.model;

public class Inmueble {
    private String nombre;
    private Double precio;

    public Inmueble(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }
}
