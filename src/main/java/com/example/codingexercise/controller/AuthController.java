package com.example.codingexercise.controller;

import com.example.codingexercise.controller.dto.LoginRequest;
import com.example.codingexercise.controller.dto.LoginResponse;
import com.example.codingexercise.model.User;
import com.example.codingexercise.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
        if (user.isEmpty() || !user.get().getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, null, null, "Invalid username or password", null));
        }

        String tokenPayload = user.get().getUsername() + ":" + System.currentTimeMillis();
        String token = Base64.getEncoder().encodeToString(tokenPayload.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok(
                new LoginResponse(true, token, user.get().getUsername(), "Login successful", user.get().getLocale()));
    }
}
