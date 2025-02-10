package es.fempa.acd.plataformacursosonline.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API.
 * Define la información y configuración para la interfaz de documentación de la API.
 */
@Configuration
public class SwaggerConfig {
    
    /**
     * Configura la documentación OpenAPI personalizada.
     * Define la información básica de la API, licencia, y servidores disponibles.
     * @return Configuración personalizada de OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        /**
         * Configuración del servidor de desarrollo
         */
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de desarrollo");

        /**
         * Configuración de la licencia
         */
        License mitLicense = new License()
                .name("Licencia MIT")
                .url("https://opensource.org/licenses/MIT");

        /**
         * Retorna la configuración completa de OpenAPI
         */
        return new OpenAPI()
                .info(new Info()
                        .title("API Plataforma Cursos Online")
                        .version("1.0")
                        .description("API para gestionar la plataforma de cursos online")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("contacto@plataformacursosonline.com"))
                        .license(mitLicense))
                .servers(List.of(devServer));
    }
}
