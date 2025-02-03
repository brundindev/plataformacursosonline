package es.fempa.acd.plataformacursosonline.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Entidad que representa un usuario en el sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de usuario único", example = "john_doe")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Contraseña encriptada del usuario")
    private String password;

    @Column(nullable = false, unique = true)
    @Schema(description = "Correo electrónico del usuario", example = "john@example.com")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Rol del usuario en el sistema", example = "ADMIN")
    private Rol rol;

    @ManyToMany
    @JoinTable(
        name = "usuario_curso",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Set<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }
}
