package com.example.repository;

import java.util.List;

import com.example.common.Product;

public interface ProductRepository {
    List<Product> findAll();
    Product save(Product product);
    void deleteById(Long id);
}
