package br.com.joao.orders_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemCreateRequest(@NotBlank String product, @NotNull @Positive Integer quantity, @NotNull @Positive BigDecimal price) {
}
