package com.example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.common.Product;

@Repository
public class PostgreSQLRepository {
    private final JdbcTemplate jdbcTemplate; 

    public PostgreSQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM product;";

        RowMapper<Product> rowMapper = new RowMapper<Product>() {
            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Product(
                    rs.getString("name"), 
                    rs.getString("description"), 
                    rs.getDouble("price"), 
                    rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null);
            }
        };

        return jdbcTemplate.query(sql, rowMapper);
    }
}
