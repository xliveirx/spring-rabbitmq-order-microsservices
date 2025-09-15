package br.com.joao.users_service.repository;

import br.com.joao.users_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByEmailIgnoreCaseAndActiveTrue(String email);
}
