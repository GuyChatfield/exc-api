package com.example.codingexercise;

import com.example.codingexercise.controller.dto.LoginRequest;
import com.example.codingexercise.controller.dto.LoginResponse;
import com.example.codingexercise.model.User;
import com.example.codingexercise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTests {

    private final TestRestTemplate restTemplate;
    private final UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private static final String TEST_USERNAME = "authtest-user";
    private static final String TEST_PASSWORD = "testpass123";

    @Autowired
    AuthControllerTests(TestRestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        // Ensure test user exists
        if (userRepository.findByUsername(TEST_USERNAME).isEmpty()) {
            User user = new User();
            user.setUsername(TEST_USERNAME);
            user.setPassword(TEST_PASSWORD);
            user.setLocale("en-US");
            userRepository.save(user);
        }
    }

    private LoginRequest createLoginRequest(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    private ResponseEntity<LoginResponse> postLogin(LoginRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange("/auth/login", HttpMethod.POST, entity, LoginResponse.class);
    }

    @Test
    void login_success() {
        LoginRequest request = createLoginRequest(TEST_USERNAME, TEST_PASSWORD);

        ResponseEntity<LoginResponse> response = postLogin(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponse body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isAuthenticated());
        assertNotNull(body.getToken());
        assertFalse(body.getToken().isBlank());
        assertEquals(TEST_USERNAME, body.getUsername());
        assertEquals("Login successful", body.getMessage());
    }

    @Test
    void login_invalidPassword() throws Exception {
        // Use MockMvc for 401 responses (TestRestTemplate has streaming issues)
        mockMvc.perform(post("/auth/login")
                .contentType(java.util.Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content("{\"username\":\"" + TEST_USERNAME + "\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.authenticated").value(false))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void login_nonexistentUser() throws Exception {
        // Use MockMvc for 401 responses (TestRestTemplate has streaming issues)
        mockMvc.perform(post("/auth/login")
                .contentType(
                        java.util.Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content("{\"username\":\"nonexistent-user-xyz\",\"password\":\"anypassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.authenticated").value(false));
    }

    @Test
    void login_returnsUserLocale() {
        // Update user locale
        User user = userRepository.findByUsername(TEST_USERNAME).orElseThrow();
        user.setLocale("en-GB");
        userRepository.save(user);

        LoginRequest request = createLoginRequest(TEST_USERNAME, TEST_PASSWORD);

        ResponseEntity<LoginResponse> response = postLogin(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("en-GB", java.util.Objects.requireNonNull(response.getBody()).getLocale());
    }
}
