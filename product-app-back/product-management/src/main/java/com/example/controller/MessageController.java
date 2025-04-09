package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.ProductResponse;
import com.example.producer.RabbitMQProducer;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private RabbitMQProducer rabbitMQProducer;

    public MessageController(RabbitMQProducer rabbitMqProducer) {
        this.rabbitMQProducer = rabbitMqProducer;
    }

    @RequestMapping("/send")
    public ResponseEntity<ProductResponse> sendMessage(@RequestParam("message") String message) {
        ProductResponse response = rabbitMQProducer.requestAllProducts();
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
