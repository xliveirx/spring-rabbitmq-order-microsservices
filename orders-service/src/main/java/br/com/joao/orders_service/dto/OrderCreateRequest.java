package br.com.joao.orders_service.dto;

import br.com.joao.orders_service.domain.OrderItem;

import java.util.List;

public record OrderCreateRequest(List<OrderItemRequest> items) {
}
