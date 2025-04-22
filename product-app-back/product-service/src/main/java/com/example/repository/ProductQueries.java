package com.example.repository;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "queries.product")
@Data
public class ProductQueries {
    private String getAll;
    private String getById;
    private String getByName;
    private String create;
    private String deleteById;
    private String updateById;
}
