package com.example.service;

import com.example.common.Product;
import com.example.common.ProductResponse;

public interface ProductService {

    ProductResponse requestAllProducts();

    ProductResponse createProduct(Product product);

    ProductResponse deleteProduct(Long productId);

    ProductResponse updateProduct(Long productId, Product product);

}