package com.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private ProductRequestAction action; // GET_ALL, GET_BY_ID, CREATE, UPDATE, DELETE
    private Long productId;
    private Product product;
}
