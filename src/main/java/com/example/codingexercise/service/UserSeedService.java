package com.example.codingexercise.service;

import com.example.codingexercise.model.User;
import com.example.codingexercise.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserSeedService implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserSeedService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        User demoUser = userRepository.findByUsername("demo-user").orElse(null);

        if (demoUser == null) {
            userRepository.save(new User("user-1", "demo-user", "demo.user@example.com", "password123", "en-US"));
            return;
        }

        if (demoUser.getPassword() == null || demoUser.getPassword().isBlank() || demoUser.getLocale() == null
                || demoUser.getLocale().isBlank()) {
            demoUser.setPassword("password123");
            demoUser.setLocale("en-US");
            userRepository.save(demoUser);
        }
    }
}
