package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.producer.RabbitMQProducer;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private RabbitMQProducer rabbitMQProducer;

    public MessageController(RabbitMQProducer rabbitMqProducer) {
        this.rabbitMQProducer = rabbitMqProducer;
    }

    @RequestMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        String response = rabbitMQProducer.sendMessage(message);
        return ResponseEntity.ok("Response: " + response);
    }
}
