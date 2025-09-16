package br.com.joao.payment_service.listener.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent(Long id, String userEmail, BigDecimal total, OrderStatus status, List<ItemsCreatedEvent> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
}

enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELED
}
