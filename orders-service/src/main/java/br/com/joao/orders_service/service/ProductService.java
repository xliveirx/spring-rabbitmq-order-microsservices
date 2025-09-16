package br.com.joao.orders_service.service;

import br.com.joao.orders_service.domain.OrderItem;
import br.com.joao.orders_service.dto.OrderItemCreateRequest;
import br.com.joao.orders_service.dto.OrderItemEditRequest;
import br.com.joao.orders_service.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {

    private final OrderItemRepository orderItemRepository;

    public ProductService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderItem createOrderItem(OrderItemCreateRequest req) {

        if(orderItemRepository.findByProductIgnoreCase(req.product()).isPresent()){
            throw new RuntimeException("Product already registered.");
        }

        if(req.quantity() <= 0){
            throw new RuntimeException("Quantity must be over than zero.");
        }

        if(req.price().compareTo(BigDecimal.ZERO) < 0){
            throw new RuntimeException("Price must be over than zero.");
        }

        return orderItemRepository.save(new OrderItem(req.product(), req.quantity(), req.price()));

    }

    @Transactional
    public OrderItem editOrderItem(OrderItemEditRequest req, Long id) {

        var item = orderItemRepository.findById(id)
                .orElseThrow();

        if(req.product() != null){
            item.setProduct(req.product());
        }

        if(req.quantity() != null && req.quantity() > 0){
            item.setQuantity(req.quantity());
        }

        if(req.price() != null && req.price().compareTo(BigDecimal.ZERO) > 0){
            item.setPrice(req.price());
        }

        return orderItemRepository.save(item);

    }

    @Transactional
    public void deleteOrderItem(Long id) {
        var item = orderItemRepository.findById(id)
                .orElseThrow();

        orderItemRepository.delete(item);
    }
}
