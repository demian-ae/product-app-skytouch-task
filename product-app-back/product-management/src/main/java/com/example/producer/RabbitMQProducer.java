package com.example.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.common.Product;
import com.example.common.ProductRequest;
import com.example.common.ProductResponse;

@Service
public class RabbitMQProducer implements ProductService {
      
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routuingKey; 

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ProductResponse requestAllProducts() { 
        ProductRequest request = new ProductRequest("GET_ALL", null, null);
        
        LOGGER.info("Sending request to RabbitMQ: {}", request);


        return (ProductResponse) rabbitTemplate.convertSendAndReceive(exchange, routuingKey, request);
    }

    @Override
    public ProductResponse createProduct(Product product) { 
        ProductRequest request = new ProductRequest("CREATE", null, product);
        
        LOGGER.info("Sending request to RabbitMQ: [{}]", request);

        Object responese = rabbitTemplate.convertSendAndReceive(exchange, routuingKey, request);

        return (ProductResponse) responese; 
    }

    @Override
    public void deleteProduct(Long productId) { 
        ProductRequest request = new ProductRequest("DELETE", productId, null);
        
        LOGGER.info("Sending request to RabbitMQ: " + request.toString());

        rabbitTemplate.convertAndSend(exchange, routuingKey, request);
    }
}
