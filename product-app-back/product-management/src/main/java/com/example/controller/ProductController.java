package com.example.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Product;
import com.example.common.ProductResponse;
import com.example.producer.RabbitMQProducer;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private RabbitMQProducer rabbitMQProducer;

    public ProductController(RabbitMQProducer rabbitMqProducer) {
        this.rabbitMQProducer = rabbitMqProducer;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        ProductResponse response = rabbitMQProducer.requestAllProducts();
        return response != null ? ResponseEntity.ok(response.getProducts()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        ProductResponse response = rabbitMQProducer.createProduct(product);
        return response != null 
            ? ResponseEntity.created(URI.create("/api/v1/products/" + response.getProducts().get(0).getId())).body(response.getProducts().get(0)) 
            : ResponseEntity.internalServerError().build();
    } 

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        rabbitMQProducer.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
