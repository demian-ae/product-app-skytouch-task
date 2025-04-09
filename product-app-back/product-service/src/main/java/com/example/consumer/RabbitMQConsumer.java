package com.example.consumer;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.common.Product;
import com.example.common.ProductRequest;
import com.example.common.ProductResponse;
import com.example.repository.ProductRepository;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    private final ProductRepository productRepository;

    public RabbitMQConsumer(ProductRepository repository) {
        this.productRepository = repository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public ProductResponse consume(ProductRequest request) { 
        LOGGER.info("Message received -> " + request.toString());

        if("getAll".equals(request.getAction())) { 
            List<Product> products = productRepository.findAll();
            return new ProductResponse(products);
        }

        return new ProductResponse(Collections.emptyList());
    }
}
