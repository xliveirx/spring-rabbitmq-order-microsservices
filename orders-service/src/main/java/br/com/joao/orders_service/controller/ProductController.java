package br.com.joao.orders_service.controller;

import br.com.joao.orders_service.dto.ProductCreateRequest;
import br.com.joao.orders_service.dto.ProductEditRequest;
import br.com.joao.orders_service.dto.ProductResponse;
import br.com.joao.orders_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createOrderItem(@RequestBody @Valid ProductCreateRequest req){

        var item = productService.createOrderItem(req);

        return ResponseEntity.ok(ProductResponse.fromDomain(item));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> editOrderItem(@PathVariable Long id, @RequestBody ProductEditRequest req){

        var item = productService.editOrderItem(req, id);

        return ResponseEntity.ok(ProductResponse.fromDomain(item));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id){

        productService.deleteOrderItem(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllItems(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size){

        var pageable = PageRequest.of(page, size);

        var response = productService.getAllItems(pageable);

        return ResponseEntity.ok(response.map(ProductResponse::fromDomain));
    }

}
