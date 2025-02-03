package es.fempa.acd.plataformacursosonline.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de desarrollo");

        License mitLicense = new License()
                .name("Licencia MIT")
                .url("https://opensource.org/licenses/MIT");

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
