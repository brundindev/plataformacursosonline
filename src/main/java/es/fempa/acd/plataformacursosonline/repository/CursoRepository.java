package es.fempa.acd.plataformacursosonline.repository;

import es.fempa.acd.plataformacursosonline.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Curso findByNombre(String nombre);
}