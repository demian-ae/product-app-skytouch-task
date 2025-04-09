package com.example.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Product;
import com.example.repository.PostgreSQLRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final PostgreSQLRepository postgreSQLRepository;

    public ProductController(PostgreSQLRepository repository) {
        this.postgreSQLRepository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(postgreSQLRepository.findAll());
    }
}
