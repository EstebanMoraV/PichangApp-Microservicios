package cl.duoc.pichangapp.users_service.config;

import cl.duoc.pichangapp.users_service.model.User;
import cl.duoc.pichangapp.users_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Inicializa el usuario administrador por defecto al arrancar el servicio.
 * Solo crea el administrador si aún no existe (idempotente).
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String ADMIN_EMAIL = "admin@pichangapp.cl";
    private static final String ADMIN_PASSWORD = "Admin@2024!";

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.existsByCorreo(ADMIN_EMAIL)) {
                log.info("Usuario administrador ya existe ({}). No se crea de nuevo.", ADMIN_EMAIL);
                return;
            }

            User admin = new User();
            admin.setCorreo(ADMIN_EMAIL);
            admin.setContrasena(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setNombre("Administrador");
            admin.setApellido("PichangApp");
            admin.setEnabled(true);
            admin.setRole("ADMIN");

            userRepository.save(admin);
            log.info("Usuario administrador creado correctamente: {}", ADMIN_EMAIL);
        };
    }
}
