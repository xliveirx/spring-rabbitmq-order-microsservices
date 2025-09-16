package br.com.joao.orders_service.listener.dto;

public record PaymentFailedEvent(Long id, String userEmail, String reason) {
}
