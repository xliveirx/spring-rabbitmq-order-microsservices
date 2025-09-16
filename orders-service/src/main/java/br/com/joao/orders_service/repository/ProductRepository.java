package br.com.joao.orders_service.repository;

import br.com.joao.orders_service.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductIgnoreCase(String product);
}
