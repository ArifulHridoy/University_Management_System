package com.university.management.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String username,
        String password,
        String department,
        String course) {
}
