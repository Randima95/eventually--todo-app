package edu.jhu.eventually.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 40, message = "Username must be 3 to 40 characters.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}