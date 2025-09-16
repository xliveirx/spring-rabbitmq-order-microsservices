package br.com.joao.orders_service.repository;

import br.com.joao.orders_service.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserEmailIgnoreCase(String userEmail, Pageable pageable);
}
