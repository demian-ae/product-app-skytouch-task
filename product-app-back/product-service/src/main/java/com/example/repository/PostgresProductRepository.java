package com.example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.common.Product;

@Repository
public class PostgresProductRepository implements ProductRepository {
    private final RowMapper<Product> productRowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null);
        }
    };
    
    private final JdbcTemplate jdbcTemplate;

    public PostgresProductRepository(JdbcTemplate jdbcTemplae) {
        this.jdbcTemplate = jdbcTemplae;
    }

    @Value("${queries.product.get_all}")
    private String getAllQuery;

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(getAllQuery, productRowMapper);
    }
}
