package com.example.codingexercise;

import com.example.codingexercise.controller.dto.UserLocaleUpdateRequest;
import com.example.codingexercise.model.User;
import com.example.codingexercise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {

    private final TestRestTemplate restTemplate;
    private final UserRepository userRepository;

    private static final String TEST_USERNAME = "locale-test-user";

    @Autowired
    UserControllerTests(TestRestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        // Ensure test user exists
        if (userRepository.findByUsername(TEST_USERNAME).isEmpty()) {
            User user = new User();
            user.setUsername(TEST_USERNAME);
            user.setPassword("testpass");
            user.setLocale("en-US");
            userRepository.save(user);
        }
    }

    @Test
    void updateLocale_success() {
        UserLocaleUpdateRequest request = new UserLocaleUpdateRequest();
        request.setLocale("en-GB");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/{username}/locale",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                TEST_USERNAME);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify the update was persisted
        User user = userRepository.findByUsername(TEST_USERNAME).orElseThrow();
        assertEquals("en-GB", user.getLocale());
    }

    @Test
    void updateLocale_toJapanese() {
        UserLocaleUpdateRequest request = new UserLocaleUpdateRequest();
        request.setLocale("ja-JP");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/{username}/locale",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                TEST_USERNAME);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        User user = userRepository.findByUsername(TEST_USERNAME).orElseThrow();
        assertEquals("ja-JP", user.getLocale());
    }

    @Test
    void updateLocale_userNotFound() {
        UserLocaleUpdateRequest request = new UserLocaleUpdateRequest();
        request.setLocale("en-GB");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/{username}/locale",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                "nonexistent-user-xyz");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateLocale_toFrench() {
        UserLocaleUpdateRequest request = new UserLocaleUpdateRequest();
        request.setLocale("fr-FR");

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/{username}/locale",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class,
                TEST_USERNAME);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        User user = userRepository.findByUsername(TEST_USERNAME).orElseThrow();
        assertEquals("fr-FR", user.getLocale());
    }
}
