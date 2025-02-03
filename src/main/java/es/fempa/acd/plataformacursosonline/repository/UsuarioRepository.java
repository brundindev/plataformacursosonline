package es.fempa.acd.plataformacursosonline.repository;

import es.fempa.acd.plataformacursosonline.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Repository
@Tag(name = "Repositorio de Usuarios", description = "Operaciones de base de datos para usuarios")
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Operation(summary = "Buscar usuario por nombre de usuario")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    Optional<Usuario> findByUsername(String username);

    @Operation(summary = "Buscar usuario por email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    Optional<Usuario> findByEmail(String email);

    @Operation(summary = "Verificar si existe un usuario por nombre de usuario")
    boolean existsByUsername(String username);

    @Operation(summary = "Verificar si existe un usuario por email")
    boolean existsByEmail(String email);
}
