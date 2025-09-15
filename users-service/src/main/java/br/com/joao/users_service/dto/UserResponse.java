package br.com.joao.users_service.dto;

import br.com.joao.users_service.domain.Role;
import br.com.joao.users_service.domain.User;

import java.time.LocalDateTime;

public record UserResponse(Long id, String fullName, String email, Role role, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), user.getActive(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
