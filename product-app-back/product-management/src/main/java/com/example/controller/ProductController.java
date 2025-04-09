package com.example.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @GetMapping
    public String hello() { 
        Product p = new Product("Jabon", "Jabon para manos", 12.0, LocalDate.now());
        return p.toString();
    }

}
