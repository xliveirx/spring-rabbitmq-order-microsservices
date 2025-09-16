package br.com.joao.orders_service.dto;

import br.com.joao.orders_service.domain.Order;
import br.com.joao.orders_service.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateResponse(
        Long id,
        String userEmail,
        BigDecimal total,
        OrderStatus status,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static OrderCreateResponse fromDomain(Order response, List<OrderItemResponse> items) {

        return new OrderCreateResponse(response.getId(), response.getUserEmail(), response.getTotal(), response.getStatus(), items, response.getCreatedAt(), response.getUpdatedAt());
    }
}
