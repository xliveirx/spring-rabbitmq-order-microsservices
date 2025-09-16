package br.com.joao.orders_service.listener.dto;

import java.math.BigDecimal;

public record PaymentApprovedEvent(Long id, String userEmail, BigDecimal total) {
}
