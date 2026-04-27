package ru.nssl.league_api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nssl.league_api.entity.User;
import ru.nssl.league_api.enums.Role;
import ru.nssl.league_api.repository.UserRepository;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // ← поменяй после первого запуска
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                log.info("✅ Администратор успешно создан!");
                log.info("   Логин: admin");
                log.info("   Пароль: admin123");
            } else {
                log.info("ℹ️ Администратор уже существует.");
            }
        };
    }
}
