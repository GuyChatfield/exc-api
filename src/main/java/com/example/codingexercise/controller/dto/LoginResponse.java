package com.example.codingexercise.controller.dto;

public class LoginResponse {
    private boolean authenticated;
    private String token;
    private String username;
    private String message;
    private String locale;

    public LoginResponse(boolean authenticated, String token, String username, String message, String locale) {
        this.authenticated = authenticated;
        this.token = token;
        this.username = username;
        this.message = message;
        this.locale = locale;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getLocale() {
        return locale;
    }
}
