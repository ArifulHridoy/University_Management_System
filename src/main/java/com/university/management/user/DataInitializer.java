package com.university.management.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder,
                                @Value("${APP_ADMIN_USERNAME:admin}") String username,
                                @Value("${APP_ADMIN_PASSWORD:admin123}") String password) {
        return args -> {
            if (!users.existsByUsername(username)) {
                users.save(new User("System Administrator", "admin@university.local", username,
                    encoder.encode(password), UserRole.ADMIN, "Administration", null));
            }
        };
    }
}
