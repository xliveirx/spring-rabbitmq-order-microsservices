package br.com.joao.orders_service.repository;

import br.com.joao.orders_service.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByProductIgnoreCase(String product);
}
