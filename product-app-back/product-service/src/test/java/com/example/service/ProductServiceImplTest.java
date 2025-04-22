package com.example.service;

import com.example.common.*;
import com.example.repository.ProductRepository;
import com.example.repository.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    private ProductRepository productRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void handleRequest_getAll_returnsAllProducts() {
        List<Product> productList = List.of(new Product(1L, "Apple", "Red Apple", 0.00, null));
        when(productRepository.findAll()).thenReturn(productList);

        ProductRequest request = new ProductRequest(ProductRequestAction.GET_ALL, null, null);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(productList, response.getProducts());
        verify(productRepository).findAll();
    }

    @Test
    void handleRequest_create_returnsCreatedProduct() {
        Product product = new Product();
        product.setName("New Product");

        when(productRepository.save(eq(product))).thenReturn(product);

        ProductRequest request = new ProductRequest(ProductRequestAction.CREATE, null, product);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(1, response.getProducts().size());
        assertEquals("New Product", response.getProducts().get(0).getName());
        verify(productRepository).save(product);
    }

    @Test
    void handleRequest_deleteById_returnsEmptyList() {
        Long id = 1L;

        when(productRepository.deleteById(id)).thenReturn(true);

        ProductRequest request = new ProductRequest(ProductRequestAction.DELETE_BY_ID, id, null);
        ProductResponse response = productService.handleRequest(request);

        assertNotNull(response.getProducts());
        assertTrue(response.getProducts().isEmpty());
        verify(productRepository).deleteById(id);
    }

    @Test
    void handleRequest_updateById_returnsUpdatedProduct() {
        Long id = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setName("Updated");

        when(productRepository.updateById(eq(id), eq(updatedProduct))).thenReturn(updatedProduct);

        ProductRequest request = new ProductRequest(ProductRequestAction.UPDATE_BY_ID, id, updatedProduct);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(1, response.getProducts().size());
        assertEquals("Updated", response.getProducts().get(0).getName());
        verify(productRepository).updateById(id, updatedProduct);
    }

    @Test
    void handleRequest_invalidAction_throwsException() {
        ProductRequest request = new ProductRequest(null, null, null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.handleRequest(request));

        assertTrue(exception.getMessage().contains("ProductRequest action must not be null"));
    }

    @Test
    void handleRequest_create_repositoryThrowsException_returnsInternalServerError() {
        Product product = new Product();
        product.setName("Failing Product");

        when(productRepository.save(any())).thenThrow(new RepositoryException("Database error"));

        ProductRequest request = new ProductRequest(ProductRequestAction.CREATE, null, product);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(ProductResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getProducts().isEmpty());
    }

    @Test
    void handleRequest_create_returnsInternalServerErrorWhenReturnedProductIsNull() {
        Product product = new Product();
        product.setName("Null Result");

        when(productRepository.save(any())).thenReturn(null);

        ProductRequest request = new ProductRequest(ProductRequestAction.CREATE, null, product);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(ProductResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getProducts().isEmpty());
    }

    @Test
    void handleRequest_updateById_returnsNotFoundWhenNull() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);

        when(productRepository.updateById(id, product)).thenReturn(null);

        ProductRequest request = new ProductRequest(ProductRequestAction.UPDATE_BY_ID, id, product);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(ProductResponseStatus.NOT_FOUND, response.getStatus());
        assertTrue(response.getProducts().isEmpty());
    }

    @Test
    void handleRequest_deleteById_returnsNotFoundWhenFalse() {
        Long id = 2L;

        when(productRepository.deleteById(id)).thenReturn(false);

        ProductRequest request = new ProductRequest(ProductRequestAction.DELETE_BY_ID, id, null);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(ProductResponseStatus.NOT_FOUND, response.getStatus());
        assertTrue(response.getProducts().isEmpty());
    }

    @Test
    void handleRequest_getAll_unexpectedException_returnsInternalServerError() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("Unexpected boom"));

        ProductRequest request = new ProductRequest(ProductRequestAction.GET_ALL, null, null);
        ProductResponse response = productService.handleRequest(request);

        assertEquals(ProductResponseStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.getProducts().isEmpty());
    }

}