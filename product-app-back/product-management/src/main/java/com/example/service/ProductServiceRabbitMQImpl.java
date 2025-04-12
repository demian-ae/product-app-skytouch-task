package com.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.common.Product;
import com.example.common.ProductRequest;
import com.example.common.ProductResponse;

@Service
public class ProductServiceRabbitMQImpl implements ProductService {
      
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceRabbitMQImpl.class);

    private final RabbitTemplate rabbitTemplate;

    public ProductServiceRabbitMQImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ProductResponse requestAllProducts() { 
        ProductRequest request = new ProductRequest("GET_ALL", null, null);
        
        LOGGER.info("Requesting all products. Sending request to RabbitMQ: {}", request);

        return (ProductResponse) rabbitTemplate.convertSendAndReceive(exchange, routingKey, request);
    }

    @Override
    public ProductResponse createProduct(Product product) { 
        ProductRequest request = new ProductRequest("CREATE", null, product);
        
        LOGGER.info("Creating a product. Sending request to RabbitMQ: {}", request);

        return (ProductResponse) rabbitTemplate.convertSendAndReceive(exchange, routingKey, request);
    }

    @Override
    public void deleteProduct(Long productId) { 
        ProductRequest request = new ProductRequest("DELETE", productId, null);
        
        LOGGER.info("Deleting a product. Sending request to RabbitMQ: {}", request);

        rabbitTemplate.convertSendAndReceive(exchange, routingKey, request);
    }
}
