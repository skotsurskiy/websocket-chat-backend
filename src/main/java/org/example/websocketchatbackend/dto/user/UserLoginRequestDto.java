package org.example.websocketchatbackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
    @Size(min = 1, max = 48)
    @NotBlank
    String username,
    @Size(min = 8, max = 48)
    @NotBlank
    String password
) {
}
