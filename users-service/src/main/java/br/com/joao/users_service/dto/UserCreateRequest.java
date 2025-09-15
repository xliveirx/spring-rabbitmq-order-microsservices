package br.com.joao.users_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(@NotBlank String fullName, @NotBlank @Email String email, @NotBlank String password, @NotBlank String confirmPassword) {
}
