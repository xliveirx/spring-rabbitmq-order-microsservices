package br.com.joao.orders_service.service;

import br.com.joao.orders_service.domain.Order;
import br.com.joao.orders_service.domain.OrderItem;
import br.com.joao.orders_service.domain.OrderStatus;
import br.com.joao.orders_service.dto.OrderCreateRequest;
import br.com.joao.orders_service.dto.OrderCreateResponse;
import br.com.joao.orders_service.dto.OrderCreatedEvent;
import br.com.joao.orders_service.dto.OrderItemResponse;
import br.com.joao.orders_service.exceptions.InsufficientStockException;
import br.com.joao.orders_service.exceptions.OrderNotFoundException;
import br.com.joao.orders_service.exceptions.ProductNotFoundException;
import br.com.joao.orders_service.listener.dto.PaymentFailedEvent;
import br.com.joao.orders_service.listener.dto.PaymentApprovedEvent;
import br.com.joao.orders_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.joao.orders_service.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, ProductService productService, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Order createOrder(OrderCreateRequest req, String userEmail) {

        var order = new Order();

        order.setUserEmail(userEmail);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        BigDecimal total = req.items().stream()
                .map(i -> {

                    var product = productService.findById(i.productId())
                            .orElseThrow(ProductNotFoundException::new);

                    var orderItem = new OrderItem();

                    orderItem.setProduct(product);
                    orderItem.setPrice(product.getPrice());
                    orderItem.setOrder(order);

                    if(product.getQuantity() < i.quantity()) throw new InsufficientStockException();

                    orderItem.setQuantity(i.quantity());

                    order.getItems().add(orderItem);

                    return product.getPrice().multiply(BigDecimal.valueOf(i.quantity()));
                })

                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        orderRepository.save(order);

        var event = OrderCreatedEvent.fromDomain(order);

        rabbitTemplate.convertAndSend(ORDER_CREATED_QUEUE, event);

        return order;
    }

    @Transactional
    public void handleApprovedPayment(PaymentApprovedEvent payload) {

        var order = orderRepository.findById(payload.id())
                .orElseThrow(OrderNotFoundException::new);

        order.setStatus(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());

        order.getItems().forEach(i -> {

            var product = productService.findById(i.getProduct().getId())
                    .orElseThrow(ProductNotFoundException::new);

            product.setQuantity(product.getQuantity() - i.getQuantity());

            productService.save(product);
        });

        orderRepository.save(order);

    }

    @Transactional
    public void handleFailedPayment(PaymentFailedEvent payload) {

        var order = orderRepository.findById(payload.id())
                .orElseThrow(OrderNotFoundException::new);

        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

    }

    @Transactional
    public Page<OrderCreateResponse> getAllUserOrders(String userEmail, Pageable pageable) {

        return orderRepository.findAllByUserEmailIgnoreCase(userEmail, pageable)
                .map(order -> {
                    var items = order.getItems().stream().map(OrderItemResponse::fromDomain)
                            .toList();

                    return OrderCreateResponse.fromDomain(order, items);
                });
    }
}
