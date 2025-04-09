package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ProductResponse> sendMessage() {
        ProductResponse response = rabbitMQProducer.requestAllProducts();
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
