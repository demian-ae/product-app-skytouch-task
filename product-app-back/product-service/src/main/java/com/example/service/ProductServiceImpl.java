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

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse handleRequest(ProductRequest request) {
        switch (request.getAction()) {
            case "GET_ALL":
                List<Product> all = findAll();
                return new ProductResponse(all);

            case "CREATE":
                Product created = save(request.getProduct());
                return new ProductResponse(created != null ? List.of(created) : Collections.emptyList());

            case "DELETE":
                deleteById(request.getProductId());
                return new ProductResponse(Collections.emptyList());

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
        return null;
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
