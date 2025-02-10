package es.fempa.acd.plataformacursosonline.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fempa.acd.plataformacursosonline.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos para acceder y manipular los datos de usuarios en la base de datos.
 */
@Repository
@Tag(name = "Repositorio de Usuarios", description = "Operaciones de base de datos para usuarios")
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario
     * @param username Nombre de usuario a buscar
     * @return Usuario encontrado o vacío
     */
    @Operation(summary = "Buscar usuario por nombre de usuario")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    Optional<Usuario> findByUsername(String username);

    @Operation(summary = "Buscar usuario por email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario dado
     * @param username Nombre de usuario a verificar
     * @return true si existe, false si no
     */
    @Operation(summary = "Verificar si existe un usuario por nombre de usuario")
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email dado
     * @param email Email a verificar
     * @return true si existe, false si no
     */
    @Operation(summary = "Verificar si existe un usuario por email")
    boolean existsByEmail(String email);
}
