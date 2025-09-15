package br.com.joao.orders_service.controller;

import br.com.joao.orders_service.dto.OrderItemCreateRequest;
import br.com.joao.orders_service.dto.OrderItemEditRequest;
import br.com.joao.orders_service.dto.OrderItemResponse;
import br.com.joao.orders_service.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-item")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItemResponse> createOrderItem(@RequestBody @Valid OrderItemCreateRequest req){

        var item = orderItemService.createOrderItem(req);

        return ResponseEntity.ok(OrderItemResponse.fromDomain(item));

    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> editOrderItem(@PathVariable Long id, @RequestBody OrderItemEditRequest req){

        var item = orderItemService.editOrderItem(req, id);

        return ResponseEntity.ok(OrderItemResponse.fromDomain(item));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id){

        orderItemService.deleteOrderItem(id);

        return ResponseEntity.noContent().build();

    }
}
