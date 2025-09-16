package br.com.joao.orders_service.controller;

import br.com.joao.orders_service.dto.OrderCreateRequest;
import br.com.joao.orders_service.dto.OrderCreateResponse;
import br.com.joao.orders_service.dto.OrderItemResponse;
import br.com.joao.orders_service.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest req, @AuthenticationPrincipal String userEmail){

        var response = orderService.createOrder(req, userEmail);

        var items = response.getItems().stream().map(OrderItemResponse::fromDomain).toList();

        var order = OrderCreateResponse.fromDomain(response, items);

        return ResponseEntity.ok(order);

    }

    @GetMapping
    public ResponseEntity<Page<OrderCreateResponse>> getAllUserOrders(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @AuthenticationPrincipal String userEmail){

        var pageable = PageRequest.of(page, size);

        var response = orderService.getAllUserOrders(userEmail, pageable);

        return ResponseEntity.ok(response);

    }
}
