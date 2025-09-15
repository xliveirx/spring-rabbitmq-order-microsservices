package br.com.joao.users_service.dto;

public record UserEditRequest(String fullName, String password, String confirmPassword) {
}
