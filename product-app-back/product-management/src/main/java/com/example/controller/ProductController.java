package com.example.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import com.example.common.ProductResponseStatus;
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
        if(response == null) return ResponseEntity.internalServerError().header("message","null response. Error reaching the service").build();
        if (response.getStatus() == ProductResponseStatus.OK) {
            return ResponseEntity.ok(response.getProducts());
        } else if (response.getStatus() == ProductResponseStatus.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.internalServerError().header("message", "error in service").build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        ProductResponse response = productService.createProduct(product);
        if(response == null) return ResponseEntity.internalServerError().header("message","null response. Error reaching the service").build();
        if(response.getStatus() == ProductResponseStatus.CREATED) {
            return ResponseEntity.created(URI.create("/api/v1/products/" + response.getProducts().get(0).getId())).body(response.getProducts().get(0));
        } else {
            return ResponseEntity.internalServerError().header("message", "error in service").build();
        }
    } 

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        ProductResponse response = productService.deleteProduct(productId);
        if(response == null) return ResponseEntity.internalServerError().header("message","null response. Error reaching the service").build();
        if(response.getStatus() == ProductResponseStatus.OK) {
            return ResponseEntity.noContent().build();
        } else if(response.getStatus() == ProductResponseStatus.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.internalServerError().header("message", "error in service").build();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        ProductResponse response = productService.updateProduct(productId, product);
        if(response == null) return ResponseEntity.internalServerError().header("message","null response. Error reaching the service").build();
        if(response.getStatus() == ProductResponseStatus.OK) {
            Product p = response.getProducts().get(0);
            return ResponseEntity.ok(p);
        } else if(response.getStatus() == ProductResponseStatus.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.internalServerError().header("message", "error in service").build();
        }
    }
}
