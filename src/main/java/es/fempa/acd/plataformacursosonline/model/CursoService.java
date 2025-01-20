package es.fempa.acd.plataformacursosonline.model;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CursoService {

    CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> getCursos() {
        return cursoRepository.findAll();
    }

}