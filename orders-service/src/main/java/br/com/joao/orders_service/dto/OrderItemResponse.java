package br.com.joao.orders_service.dto;

import br.com.joao.orders_service.domain.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(Long id, String product, Integer quantity, BigDecimal price) {
    public static OrderItemResponse fromDomain(OrderItem item) {
        return new OrderItemResponse(item.getId(), item.getProduct(), item.getQuantity(), item.getPrice());
    }
}
