package es.fempa.acd.plataformacursosonline.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.repository.CursoRepository;
import es.fempa.acd.plataformacursosonline.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Servicio que gestiona todas las operaciones relacionadas con usuarios.
 * Maneja la lógica de negocio para crear, modificar y gestionar usuarios.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CursoRepository cursoRepository;

    /**
     * Constructor que inyecta las dependencias necesarias
     */
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, CursoRepository cursoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.cursoRepository = cursoRepository;
    }

    /**
     * Crea un nuevo usuario en el sistema
     * @throws IllegalArgumentException si el username o email ya existen
     */
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario crearUsuario(String username, String password, String email, Rol rol) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmail(email);
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Operation(summary = "Buscar usuario por nombre de usuario")
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
		
	}

    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESOR') || hasRole('ESTUDIANTE')")
    public void editarUsuario(Long id, String username, String email, String password, Rol rol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
                
        if (!usuario.getUsername().equals(username) && existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (!usuario.getEmail().equals(email) && existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        
        usuario.setUsername(username);
        usuario.setEmail(email);
        
        if (password != null && !password.isEmpty() && !password.equals(usuario.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(password));
        } else {
            usuario.setPassword(password);
        }
        
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
    }


    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario registerUsuario(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        return usuarioRepository.save(usuario);
    }

    public void inscribirUsuarioEnCurso(Long usuarioId, Long cursoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
        
        usuario.getCursos().add(curso);
        usuarioRepository.save(usuario);
    }

    @Operation(summary = "Verificar si un usuario está inscrito en un curso")
    public boolean estaInscritoEnCurso(Long usuarioId, Long cursoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return usuario.getCursos().stream().anyMatch(curso -> curso.getId().equals(cursoId));
    }

    public void desinscribirUsuarioDeCurso(Long usuarioId, Long cursoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        usuario.getCursos().remove(curso);
        usuarioRepository.save(usuario);
    }

    @PreAuthorize("hasRole('ADMIN') || hasRole('PROFESOR') || hasRole('ESTUDIANTE')")
    public void cambiarContraseña(Long userId, String contraseñaActual, String nuevaContraseña) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(contraseñaActual, usuario.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        usuarioRepository.save(usuario);
    }
}
