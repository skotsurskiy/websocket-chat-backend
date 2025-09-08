package org.example.websocketchatbackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.websocketchatbackend.validation.FieldMatch;

@FieldMatch(
    field = "password",
    fieldMatch = "confirmPassword"
)
public record UserRegisterRequestDto(
    @Size(min = 1, max = 48)
    @NotBlank
    String username,
    @Size(min = 1, max = 48)
    @NotBlank
    String password,
    @Size(min = 1, max = 48)
    @NotBlank
    String confirmPassword
) {
}
