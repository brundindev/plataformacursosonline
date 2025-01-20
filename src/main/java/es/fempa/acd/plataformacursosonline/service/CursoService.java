package es.fempa.acd.plataformacursosonline.service;

import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public void crearCurso(String nombre, String descripcion, double precio) {
        if (cursoRepository.findByNombre(nombre) == null) {
            Curso curso = new Curso(nombre, descripcion, precio);
            cursoRepository.save(curso);
        } else {
            throw new IllegalArgumentException("El curso ya existe.");
        }
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public void actualizarCurso(Curso curso) {
        cursoRepository.save(curso);
    }

    public void eliminarCurso(Long id) {
        cursoRepository.deleteById(id);
    }
}