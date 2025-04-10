package com.example.producer;

import com.example.common.Product;
import com.example.common.ProductResponse;

public interface ProductService {

    ProductResponse requestAllProducts();

    ProductResponse createProduct(Product product);

    void deleteProduct(Long productId);

}