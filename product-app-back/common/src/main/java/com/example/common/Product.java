package com.example.common;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private Long id;
    private String name; 
    private String description;
    private double price;
    private LocalDate expirationDate;
}
