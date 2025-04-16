package com.example.service;

import com.example.common.Product;
import com.example.common.ProductRequest;
import com.example.common.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse handleRequest(ProductRequest request);
    List<Product> findAll();
    Product save(Product product);
    Product updateById(Long id, Product product);
    boolean deleteById(Long id);
}
