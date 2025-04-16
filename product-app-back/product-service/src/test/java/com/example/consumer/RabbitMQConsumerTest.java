package com.example.consumer;

import com.example.common.ProductRequest;
import com.example.common.ProductResponse;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RabbitMQConsumerTest {
    private ProductService productService;
    private RabbitMQConsumer rabbitMQConsumer;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        rabbitMQConsumer = new RabbitMQConsumer(productService);
    }

    @Test
    void consume_validRequest_returnsResponse() {
        // Arrange
        ProductRequest request = new ProductRequest();
        ProductResponse expectedResponse = new ProductResponse();

        when(productService.handleRequest(request)).thenReturn(expectedResponse);

        // Act
        ProductResponse actualResponse = rabbitMQConsumer.consume(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(productService).handleRequest(request);
    }

    @Test
    void consume_whenExceptionThrown_logsAndRethrows() {
        // Arrange
        ProductRequest request = new ProductRequest();
        RuntimeException exception = new RuntimeException("Test error");

        when(productService.handleRequest(request)).thenThrow(exception);

        // Act + Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> rabbitMQConsumer.consume(request));
        assertEquals("Test error", thrown.getMessage());
        verify(productService).handleRequest(request);
    }

}