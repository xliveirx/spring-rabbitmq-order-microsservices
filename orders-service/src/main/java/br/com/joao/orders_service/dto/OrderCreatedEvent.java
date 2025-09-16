package br.com.joao.orders_service.dto;

import br.com.joao.orders_service.domain.Order;
import br.com.joao.orders_service.domain.OrderItem;
import br.com.joao.orders_service.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent( Long id,
                                 String userEmail,
                                 BigDecimal total,
                                 OrderStatus status,
                                 List<OrderItemResponse> items,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
    public static OrderCreatedEvent fromDomain(Order order) {
        return new OrderCreatedEvent(order.getId(), order.getUserEmail(), order.getTotal(), order.getStatus(),
            order.getItems().stream().map(OrderItemResponse::fromDomain).toList(), order.getCreatedAt(), order.getUpdatedAt());
    }
}
