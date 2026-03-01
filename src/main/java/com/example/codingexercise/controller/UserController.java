package com.example.codingexercise.controller;

import com.example.codingexercise.controller.dto.UserLocaleUpdateRequest;
import com.example.codingexercise.model.User;
import com.example.codingexercise.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{username}/locale")
    public ResponseEntity<Void> updateLocale(@PathVariable String username,
            @RequestBody UserLocaleUpdateRequest localeUpdateRequest) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        user.get().setLocale(localeUpdateRequest.getLocale());
        userRepository.save(Objects.requireNonNull(user.get()));
        return ResponseEntity.noContent().build();
    }
}
