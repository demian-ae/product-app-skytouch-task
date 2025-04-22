package com.example.service;

import com.example.common.ProductRequestAction;
import com.example.config.RabbitMQProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    private void validateProduct(Product product) {
        if (product == null) {
            throw new InvalidProductException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new InvalidProductException("Product name must not be empty");
        }
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new InvalidProductException("Product description must not be empty");
        }
        if (product.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than zero");
        }
    }

    @Override
    public ProductResponse requestAllProducts() { 
        ProductRequest request = new ProductRequest(ProductRequestAction.GET_ALL, null, null);
        
        LOGGER.info("Requesting all products. Sending request to RabbitMQ: {}", request);

        return (ProductResponse) rabbitTemplate.convertSendAndReceive(rabbitMQProperties.getExchange(), rabbitMQProperties.getRoutingKey(), request);
    }

    @Override
    public ProductResponse createProduct(Product product) {
        validateProduct(product);

        ProductRequest request = new ProductRequest(ProductRequestAction.CREATE, null, product);
        
        LOGGER.info("Creating a product. Sending request to RabbitMQ: {}", request);

        return (ProductResponse) rabbitTemplate.convertSendAndReceive(rabbitMQProperties.getExchange(), rabbitMQProperties.getRoutingKey(), request);
    }

    @Override
    public ProductResponse deleteProduct(Long productId) {
        ProductRequest request = new ProductRequest(ProductRequestAction.DELETE_BY_ID, productId, null);
        
        LOGGER.info("Deleting a product. Sending request to RabbitMQ: {}", request);

       return (ProductResponse) rabbitTemplate.convertSendAndReceive(rabbitMQProperties.getExchange(), rabbitMQProperties.getRoutingKey(), request);
    }

    @Override
    public ProductResponse updateProduct(Long productId, Product product) {
        validateProduct(product);

        ProductRequest request = new ProductRequest(ProductRequestAction.UPDATE_BY_ID, productId, product);

        LOGGER.info("Updating a product with id {} and values: {}", productId, product);

        return (ProductResponse) rabbitTemplate.convertSendAndReceive(rabbitMQProperties.getExchange(), rabbitMQProperties.getRoutingKey(), request);
    }
}
