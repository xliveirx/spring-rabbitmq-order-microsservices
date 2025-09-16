package br.com.joao.orders_service.dto;

public record OrderItemRequest(Long productId, Integer quantity) {
}
