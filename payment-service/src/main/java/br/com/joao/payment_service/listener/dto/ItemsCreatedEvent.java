package br.com.joao.payment_service.listener.dto;

import java.math.BigDecimal;

public record ItemsCreatedEvent(String productName, BigDecimal price, Integer quantity) {
}
