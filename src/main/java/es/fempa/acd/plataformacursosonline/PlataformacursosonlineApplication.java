package es.fempa.acd.plataformacursosonline;

import org.h2.tools.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import es.fempa.acd.plataformacursosonline.model.Curso;
import es.fempa.acd.plataformacursosonline.model.Rol;
import es.fempa.acd.plataformacursosonline.model.Usuario;
import es.fempa.acd.plataformacursosonline.repository.CursoRepository;
import es.fempa.acd.plataformacursosonline.repository.UsuarioRepository;
import org.tinylog.Logger;

import java.sql.SQLException;

@SpringBootApplication
public class PlataformacursosonlineApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PlataformacursosonlineApplication.class, args);
	}

	private static void startDatabase() throws SQLException {
		new Server().runTool("-tcp", "-web", "-ifNotExists");
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			startDatabase();
			Logger.info("Base de datos inicializada...");
		} catch (Exception e) {
			Logger.error(e, "Error al intentar inicializar la base de datos.");
		}
	}

	@Bean
	public CommandLineRunner insertCursos(CursoRepository cursoRepository) {
		return (args) -> {
			if (cursoRepository.findByNombre("Curso de Programación Java") == null) {
				Curso curso1 = new Curso();
				curso1.setNombre("Curso de Programación Java");
				curso1.setDescripcion("Aprende a programar en Java desde cero.");
				curso1.setPrecio(150.0);
				cursoRepository.save(curso1);
			}

			if (cursoRepository.findByNombre("Curso de Desarrollo Web") == null) {
				Curso curso2 = new Curso();
				curso2.setNombre("Curso de Desarrollo Web");
				curso2.setDescripcion("Crea sitios web modernos y responsivos.");
				curso2.setPrecio(200.0);
				cursoRepository.save(curso2);
			}

			if (cursoRepository.findByNombre("Curso de Bases de Datos") == null) {
				Curso curso3 = new Curso();
				curso3.setNombre("Curso de Bases de Datos");
				curso3.setDescripcion("Domina el manejo de bases de datos SQL.");
				curso3.setPrecio(120.0);
				cursoRepository.save(curso3);
			}
		};
	}

	@Bean
	public CommandLineRunner insertUser(UsuarioRepository usuarioRepository) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("123");

		return (args) -> {
			if (!usuarioRepository.existsByEmail("admin@gmail.com")) {
				Usuario usuario = new Usuario();
				usuario.setUsername("admin");
				usuario.setEmail("admin@gmail.com");
				usuario.setPassword(hashedPassword);
				usuario.setRol(Rol.ADMIN);
				usuarioRepository.save(usuario);
			} else {
				Logger.info("El usuario admin ya existe.");
			}
		};
	}
}
