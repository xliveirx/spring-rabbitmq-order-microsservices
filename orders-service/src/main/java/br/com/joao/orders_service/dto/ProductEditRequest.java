package br.com.joao.orders_service.dto;

import java.math.BigDecimal;

public record ProductEditRequest(String product, Integer quantity, BigDecimal price) {
}
