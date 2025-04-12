package com.example.consumer;

import com.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.common.ProductRequest;
import com.example.common.ProductResponse;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final ProductService productService;

    public RabbitMQConsumer(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = "${rabbitmq.queue}", ackMode = "AUTO")
    public ProductResponse consume(ProductRequest request) {
        LOGGER.info("Message received -> {}", request.toString());
        try {
            return productService.handleRequest(request);
        } catch (Exception e) {
            LOGGER.error("Error while getting the request", e);
            throw e; // causing a negative ack
        }
    }
}
