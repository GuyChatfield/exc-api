package com.example.codingexercise.service;

import com.example.codingexercise.model.User;
import com.example.codingexercise.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class UserSeedService implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserSeedService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        List<User> seedUsers = List.of(
                new User("user-1", "demo-user", "demo.user@example.com", "password123", "en-US"),
                new User("user-2", "tuser", "tuser@example.com", "tpass", "en-US"));

        for (User user : seedUsers) {
            User existing = userRepository.findByUsername(user.getUsername()).orElse(null);
            if (existing == null) {
                userRepository.save(user);
            } else if (existing.getPassword() == null || existing.getPassword().isBlank()
                    || existing.getLocale() == null || existing.getLocale().isBlank()) {
                existing.setPassword(user.getPassword());
                existing.setLocale(user.getLocale());
                userRepository.save(existing);
            }
        }
    }
}
