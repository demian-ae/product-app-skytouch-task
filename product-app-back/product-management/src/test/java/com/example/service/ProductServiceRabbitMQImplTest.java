package com.example.service;

import com.example.common.Product;
import com.example.common.ProductRequest;
import com.example.common.ProductRequestAction;
import com.example.common.ProductResponse;
import com.example.config.RabbitMQProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceRabbitMQImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitMQProperties rabbitMQProperties;

    private final String exchange = "test.exchange";
    private final String routingKey = "test.routing.key";

    @InjectMocks
    private ProductServiceRabbitMQImpl productService;

    @Test
    void requestAllProducts_sendsCorrectMessageAndReturnsResponse() {
        // Arrange
        ProductResponse expectedResponse = new ProductResponse();

        when(rabbitMQProperties.getExchange()).thenReturn(exchange);
        when(rabbitMQProperties.getRoutingKey()).thenReturn(routingKey);
        when(rabbitTemplate.convertSendAndReceive(eq(exchange), eq(routingKey), any(ProductRequest.class)))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse actualResponse = productService.requestAllProducts();

        // Assert
        assertSame(expectedResponse, actualResponse);

        ArgumentCaptor<ProductRequest> captor = ArgumentCaptor.forClass(ProductRequest.class);
        verify(rabbitTemplate).convertSendAndReceive(eq(exchange), eq(routingKey), captor.capture());

        ProductRequest sentRequest = captor.getValue();
        assertEquals(ProductRequestAction.GET_ALL, sentRequest.getAction());
        assertNull(sentRequest.getProductId());
        assertNull(sentRequest.getProduct());
    }

    @Test
    void createProduct_sendsCorrectMessageAndReturnsResponse() {
        // Arrange
        Product product = new Product(null, "Product A", "Description of the product", 9.99, LocalDate.now()); // populate if needed
        ProductResponse expectedResponse = new ProductResponse();

        when(rabbitMQProperties.getExchange()).thenReturn(exchange);
        when(rabbitMQProperties.getRoutingKey()).thenReturn(routingKey);
        when(rabbitTemplate.convertSendAndReceive(eq(exchange), eq(routingKey), any(ProductRequest.class)))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse actualResponse = productService.createProduct(product);

        // Assert
        assertSame(expectedResponse, actualResponse);

        ArgumentCaptor<ProductRequest> captor = ArgumentCaptor.forClass(ProductRequest.class);
        verify(rabbitTemplate).convertSendAndReceive(eq(exchange), eq(routingKey), captor.capture());

        ProductRequest sentRequest = captor.getValue();
        assertEquals(ProductRequestAction.CREATE, sentRequest.getAction());
        assertEquals(product, sentRequest.getProduct());
        assertNull(sentRequest.getProductId());
    }

    @Test
    void deleteProduct_sendsCorrectMessage() {
        // Arrange
        Long productId = 123L;

        when(rabbitMQProperties.getExchange()).thenReturn(exchange);
        when(rabbitMQProperties.getRoutingKey()).thenReturn(routingKey);

        // Act
        productService.deleteProduct(productId);

        // Assert
        ArgumentCaptor<ProductRequest> captor = ArgumentCaptor.forClass(ProductRequest.class);
        verify(rabbitTemplate).convertSendAndReceive(eq(exchange), eq(routingKey), captor.capture());

        ProductRequest sentRequest = captor.getValue();
        assertEquals(ProductRequestAction.DELETE_BY_ID, sentRequest.getAction());
        assertEquals(productId, sentRequest.getProductId());
        assertNull(sentRequest.getProduct());
    }

    @Test
    void updateProduct_sendsCorrectMessageAndReturnsResponse() {
        // Arrange
        Long productId = 456L;
        Product product = new Product(null, "Product A", "Description of the product", 9.99, LocalDate.now()); // populate if needed
        ProductResponse expectedResponse = new ProductResponse();

        when(rabbitMQProperties.getExchange()).thenReturn(exchange);
        when(rabbitMQProperties.getRoutingKey()).thenReturn(routingKey);
        when(rabbitTemplate.convertSendAndReceive(eq(exchange), eq(routingKey), any(ProductRequest.class)))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse actualResponse = productService.updateProduct(productId, product);

        // Assert
        assertSame(expectedResponse, actualResponse);

        ArgumentCaptor<ProductRequest> captor = ArgumentCaptor.forClass(ProductRequest.class);
        verify(rabbitTemplate).convertSendAndReceive(eq(exchange), eq(routingKey), captor.capture());

        ProductRequest sentRequest = captor.getValue();
        assertEquals(ProductRequestAction.UPDATE_BY_ID, sentRequest.getAction());
        assertEquals(productId, sentRequest.getProductId());
        assertEquals(product, sentRequest.getProduct());
    }
}