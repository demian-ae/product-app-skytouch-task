package com.example.repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.common.Product;

@Repository
public class PostgresProductRepository implements ProductRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepository.class);
    private final RowMapper<Product> productRowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getLong("id"), 
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null);
        }
    };
    
    private final JdbcTemplate jdbcTemplate;

    public PostgresProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value("${queries.product.get_all}")
    private String getAllQuery;

    @Override
    public List<Product> findAll() {
        try { 
            LOGGER.info("Executing query: fetch all products");
            return jdbcTemplate.query(getAllQuery, productRowMapper);
        } catch (DataAccessException e) {
            LOGGER.error("Error fetching products from database", e);
            throw new RepositoryException("Error fetching products from database", e);
        }
    }

    @Value("${queries.product.create}")
    private String createQuery; 

    @Override
    public void save(Product product) { 
        try {
            LOGGER.info("Executing query: insert product [{}]", product.getName());
            jdbcTemplate.update(
                createQuery,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getExpirationDate() != null ? Date.valueOf(product.getExpirationDate()) : null // java.Date to sql.Date
            );
        } catch (DataAccessException e) {
            LOGGER.error("Error inserting product into database", e);
            throw new RepositoryException("Error inserting product into database", e);
        }
    }
}
