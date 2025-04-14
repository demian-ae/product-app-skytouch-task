package com.example.controller;

import java.net.URI;
import java.util.List;

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
        return response != null ? ResponseEntity.ok(response.getProducts()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        ProductResponse response = productService.createProduct(product);
        return response != null 
            ? ResponseEntity.created(URI.create("/api/v1/products/" + response.getProducts().get(0).getId())).body(response.getProducts().get(0)) 
            : ResponseEntity.internalServerError().build();
    } 

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        productService.updateProduct(productId, product);
        return ResponseEntity.noContent().build();
    }
}
