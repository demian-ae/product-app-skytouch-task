package com.example.service;

import com.example.common.ProductRequestAction;
import com.example.config.RabbitMQProperties;
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

    private final RabbitMQProperties rabbitMQProperties;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceRabbitMQImpl.class);

    private final RabbitTemplate rabbitTemplate;

    public ProductServiceRabbitMQImpl(RabbitTemplate rabbitTemplate, RabbitMQProperties rabbitMQProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Override
    public ProductResponse requestAllProducts() { 
        ProductRequest request = new ProductRequest(ProductRequestAction.GET_ALL, null, null);
        
        LOGGER.info("Requesting all products. Sending request to RabbitMQ: {}", request);

        return (ProductResponse) rabbitTemplate.convertSendAndReceive(rabbitMQProperties.getExchange(), rabbitMQProperties.getRoutingKey(), request);
    }

    @Override
    public ProductResponse createProduct(Product product) { 
        ProductRequest request = new ProductRequest(ProductRequestAction.CREATE, null, product);
        
        LOGGER.info("Creating a product. Sending request to RabbitMQ: {}", request);

        return (ProductResponse) rabbitTemplate.convertSendAndReceive(rabbitMQProperties.getExchange(), rabbitMQProperties.getRoutingKey(), request);
    }

    @Override
    public void deleteProduct(Long productId) { 
        ProductRequest request = new ProductRequest(ProductRequestAction.DELETE_BY_ID, productId, null);
        
        LOGGER.info("Deleting a product. Sending request to RabbitMQ: {}", request);

        rabbitTemplate.convertSendAndReceive(rabbitMQProperties.getExchange(), rabbitMQProperties.getRoutingKey(), request);
    }
}
