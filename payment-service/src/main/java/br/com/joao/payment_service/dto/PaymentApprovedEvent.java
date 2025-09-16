package br.com.joao.payment_service.dto;

import java.math.BigDecimal;

public record PaymentApprovedEvent(Long id, String userEmail, BigDecimal total) {
}
