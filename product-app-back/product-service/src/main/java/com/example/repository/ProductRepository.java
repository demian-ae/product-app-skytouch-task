package com.example.repository;

import java.util.List;

import com.example.common.Product;

public interface ProductRepository {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    boolean deleteById(Long id);
    Product updateById(Long id, Product product);
}
