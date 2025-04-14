package com.example.service;

import com.example.common.Product;
import com.example.common.ProductRequest;
import com.example.common.ProductResponse;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse handleRequest(ProductRequest request) {
        switch (request.getAction()) {
            case GET_ALL:
                List<Product> all = findAll();
                return new ProductResponse(all);

            case CREATE:
                Product created = save(request.getProduct());
                return new ProductResponse(created != null ? List.of(created) : Collections.emptyList());

            case DELETE_BY_ID:
                deleteById(request.getProductId());
                return new ProductResponse(Collections.emptyList());

            case UPDATE_BY_ID:
                Product updated = updateById(request.getProductId(), request.getProduct());
                return new ProductResponse(List.of(updated));

            default:
                throw new IllegalArgumentException("Unsupported request type: " + request.getAction());
        }
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateById(Long id, Product product) {
        return productRepository.updateById(id, product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
