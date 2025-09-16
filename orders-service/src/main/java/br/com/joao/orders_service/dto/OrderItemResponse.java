package br.com.joao.orders_service.dto;

import br.com.joao.orders_service.domain.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(String productName, BigDecimal price, Integer quantity) {
    public static OrderItemResponse fromDomain(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getProduct().getProduct(), orderItem.getPrice(), orderItem.getQuantity());
    }
}
