package br.com.joao.payment_service.dto;

public record PaymentFailedEvent(Long id, String userEmail, String reason) {
}
