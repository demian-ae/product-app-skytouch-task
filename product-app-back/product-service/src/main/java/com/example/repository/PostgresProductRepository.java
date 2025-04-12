package com.example.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.common.Product;

@Repository
public class PostgresProductRepository implements ProductRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresProductRepository.class);

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null
                );


    private final JdbcTemplate jdbcTemplate;

    public PostgresProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value("${queries.product.get_all}")
    private String getAllQuery;

    @Override
    public List<Product> findAll() {
        try {
            LOGGER.debug("Executing query: fetch all products");
            return jdbcTemplate.query(getAllQuery, productRowMapper);
        } catch (DataAccessException e) {
            LOGGER.error("Error fetching products from database", e);
            throw new RepositoryException("Error fetching products from database", e);
        }
    }

    @Value("${queries.product.create}")
    private String createQuery;

    @Override
    public Product save(Product product) {
        try {
            LOGGER.info("Executing query: insert product {} ", product.getName());

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setDouble(3, product.getPrice());
                if (product.getExpirationDate() != null) {
                    ps.setDate(4, Date.valueOf(product.getExpirationDate()));
                } else {
                    ps.setNull(4, Types.DATE);
                }
                return ps;
            }, keyHolder);

            // extract the generated ID and set it into the product
            Map<String, Object> keys = keyHolder.getKeys();
            if (keys != null && keys.containsKey("id")) {
                Long generatedId = ((Number) keys.get("id")).longValue();
                return new Product(
                        generatedId,
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getExpirationDate());
            } else {
                return null; 
            }
        } catch (DataAccessException e) {
            LOGGER.error("Error inserting product into database", e);
            throw new RepositoryException("Error inserting product into database", e);
        }
    }

    @Value("${queries.product.delete}")
    private String deleteQuery; 

    public void deleteById(Long id) { 
        try {
            LOGGER.info("Executing query: delete product with id: {}", id);
            jdbcTemplate.update(deleteQuery, id);
        } catch (DataAccessException e) {
            LOGGER.error("Error deleting product from database", e);
            //throw new RepositoryException("Error deleting product from database", e);
        }
    }
}
