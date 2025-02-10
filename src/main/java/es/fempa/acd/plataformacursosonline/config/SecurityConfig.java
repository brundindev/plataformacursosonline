package es.fempa.acd.plataformacursosonline.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import es.fempa.acd.plataformacursosonline.service.CustomUserDetailsService;

/**
 * Configuración de seguridad de la aplicación.
 * Define las reglas de acceso y la configuración de autenticación.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Servicio personalizado que implementa la lógica para cargar los usuarios
     * desde nuestra fuente de datos (ej: base de datos)
     */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * Este es el método principal que define cómo se comporta la seguridad en nuestra aplicación.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Define las reglas de autorización para las diferentes URLs
            .authorizeHttpRequests(auth -> auth
                // Estas rutas serán accesibles sin autenticación
                .requestMatchers("/login", "/registro", "/css/**", "/js/**").permitAll()
                // Cualquier otra ruta requerirá autenticación
                .anyRequest().authenticated()
            )
            // Configura el formulario de login personalizado
            .formLogin(form -> form
                // Especifica la ruta a nuestra página de login personalizada
                .loginPage("/login")
                // Configura qué hacer cuando falla el login
                .failureHandler((request, response, exception) -> {
                    // Obtiene el nombre de usuario que intentó hacer login
                    String username = request.getParameter("username");
                    // Redirige a la página de login con un mensaje de error
                    String redirectUrl = "/login?error=" + username;
                    response.sendRedirect(redirectUrl);
                })
                // Permite acceso público al formulario de login
                .permitAll()
            )
            // Configura el proceso de logout
            .logout(logout -> logout
                // Después del logout, redirige al login
                .logoutSuccessUrl("/login")
                // Permite que cualquiera pueda hacer logout
                .permitAll()
            );
        
        return http.build();
    }

    /**
     * Configura el codificador de contraseñas.
     * BCrypt es un algoritmo de hash seguro para contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el proveedor de autenticación.
     * Este componente une el servicio de usuarios y el codificador de contraseñas
     * para realizar el proceso de autenticación.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Establece el servicio que se usará para cargar los usuarios
        authProvider.setUserDetailsService(userDetailsService);
        // Establece el codificador para verificar las contraseñas
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura el gestor de autenticación.
     * Este es el componente central que maneja todo el proceso de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
