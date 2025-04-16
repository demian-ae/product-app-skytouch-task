package com.example.controller;

import com.example.common.Product;
import com.example.common.ProductResponse;
import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getProducts_returnsProductList() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Apple");

        ProductResponse response = new ProductResponse();
        response.setProducts(List.of(product));

        when(productService.requestAllProducts()).thenReturn(response);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }

    @Test
    void getProducts_returnsNotFoundIfResponseIsNull() throws Exception {
        when(productService.requestAllProducts()).thenReturn(null);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_returnsCreatedProduct() throws Exception {
        Product inputProduct = new Product();
        inputProduct.setName("Apple");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Apple");

        ProductResponse response = new ProductResponse();
        response.setProducts(List.of(savedProduct));

        when(productService.createProduct(any(Product.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\": \"New Product\"" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/products/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void createProduct_returnsServerErrorIfNullResponse() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Product\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteProduct_returnsNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateProduct_returnsNoContent() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(new ProductResponse());

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated\"}"))
                .andExpect(status().isNoContent());
    }
}