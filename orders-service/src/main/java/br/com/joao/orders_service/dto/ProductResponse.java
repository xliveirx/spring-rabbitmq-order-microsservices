package br.com.joao.orders_service.dto;

import br.com.joao.orders_service.domain.OrderItem;
import br.com.joao.orders_service.domain.Product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String product, Integer quantity, BigDecimal price) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(product.getId(), product.getProduct(), product.getQuantity(), product.getPrice());
    }
}
