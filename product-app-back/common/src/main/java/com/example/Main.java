package com.example;

import com.example.common.Product;

public class Main {
    public static void main(String[] args) {
        Product p = new Product(1L, "Laptop", "A high-end laptop", 1500.00, null);
        System.out.println(p);
        System.out.println("Hello world!");
    }
}