package br.com.joao.orders_service.service;

import br.com.joao.orders_service.domain.Product;
import br.com.joao.orders_service.dto.ProductCreateRequest;
import br.com.joao.orders_service.dto.ProductEditRequest;
import br.com.joao.orders_service.exceptions.NullPriceException;
import br.com.joao.orders_service.exceptions.NullQuantityException;
import br.com.joao.orders_service.exceptions.ProductAlreadyRegisteredException;
import br.com.joao.orders_service.exceptions.ProductNotFoundException;
import br.com.joao.orders_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createOrderItem(ProductCreateRequest req) {

        if(productRepository.findByProductIgnoreCase(req.product()).isPresent()){
            throw new ProductAlreadyRegisteredException();
        }

        if(req.quantity() <= 0){
            throw new NullQuantityException();
        }

        if(req.price().compareTo(BigDecimal.ZERO) < 0){
            throw new NullPriceException();
        }

        return productRepository.save(new Product(req.product(), req.quantity(), req.price()));

    }

    @Transactional
    public Product editOrderItem(ProductEditRequest req, Long id) {

        var item = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if(req.product() != null){
            item.setProduct(req.product());
        }

        if(req.quantity() != null && req.quantity() > 0){
            item.setQuantity(req.quantity());
        }

        if(req.price() != null && req.price().compareTo(BigDecimal.ZERO) > 0){
            item.setPrice(req.price());
        }

        return productRepository.save(item);

    }

    @Transactional
    public void deleteOrderItem(Long id) {
        var item = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(item);
    }

    public Page<Product> getAllItems(PageRequest pageable) {

        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long id) {

        return productRepository.findById(id);
    }

    public void save(Product product) {
        productRepository.save(product);
    }
}
