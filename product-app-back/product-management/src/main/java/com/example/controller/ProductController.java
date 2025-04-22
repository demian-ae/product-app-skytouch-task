package com.example.controller;

import java.net.URI;
import java.util.List;

import com.example.common.ProductResponseStatus;
import com.example.service.ResourceNotFoundException;
import com.example.service.ServiceUnavailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.common.Product;
import com.example.common.ProductResponse;
import com.example.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        ProductResponse response = productService.requestAllProducts();

        if(response == null) throw new ServiceUnavailableException("Null response. Error reaching the service");

        if (response.getStatus() == ProductResponseStatus.OK) {
            return ResponseEntity.ok(response.getProducts());
        } else if (response.getStatus() == ProductResponseStatus.NOT_FOUND) {
            throw new ResourceNotFoundException("No products found");
        } else {
            throw new ServiceUnavailableException("Unexpected error in service");
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        ProductResponse response = productService.createProduct(product);

        if(response == null) throw new ServiceUnavailableException("Null response. Error reaching the service");

        if(response.getStatus() == ProductResponseStatus.CREATED) {
            return ResponseEntity.created(URI.create("/api/v1/products/" + response.getProducts().get(0).getId())).body(response.getProducts().get(0));
        } else {
            throw new ServiceUnavailableException("Unexpected error in service");
        }
    } 

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        ProductResponse response = productService.deleteProduct(productId);

        if(response == null) throw new ServiceUnavailableException("Null response. Error reaching the service");

        if(response.getStatus() == ProductResponseStatus.OK) {
            return ResponseEntity.noContent().build();
        } else if(response.getStatus() == ProductResponseStatus.NOT_FOUND) {
            throw new ResourceNotFoundException("No products found");
        } else {
            throw new ServiceUnavailableException("Unexpected error in service");
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        ProductResponse response = productService.updateProduct(productId, product);

        if(response == null) throw new ServiceUnavailableException("Null response. Error reaching the service");

        if(response.getStatus() == ProductResponseStatus.OK) {
            Product p = response.getProducts().get(0);
            return ResponseEntity.ok(p);
        } else if(response.getStatus() == ProductResponseStatus.NOT_FOUND) {
            throw new ResourceNotFoundException("No products found");
        } else {
            throw new ServiceUnavailableException("Unexpected error in service");
        }
    }
}
