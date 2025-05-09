package com.example.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ProductQueries productQueries;

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getDate("expiration_date") != null ? rs.getDate("expiration_date").toLocalDate() : null
                );


    private final JdbcTemplate jdbcTemplate;

    public PostgresProductRepository(JdbcTemplate jdbcTemplate, ProductQueries productQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.productQueries = productQueries;
    }

    @Override
    public List<Product> findAll() {
        try {
            LOGGER.debug("Executing query: fetch all products");
            return jdbcTemplate.query(productQueries.getGetAll(), productRowMapper);
        } catch (DataAccessException e) {
            LOGGER.error("Error fetching products from database", e);
            throw new RepositoryException("Error fetching products from database", e);
        }
    }

    @Override
    public Product findById(Long id) {
        try {
            LOGGER.info("Executing query find by id");
            List<Product> res = jdbcTemplate.query(productQueries.getGetById(), productRowMapper, id);
            if(res.isEmpty()) return null;
            return res.get(0);
        } catch (DataAccessException e) {
            LOGGER.error("Error fetching product from database", e);
            throw new RepositoryException("Error fetching product from database", e);
        }
    }

    @Override
    public Product save(Product product) {
        try {
            LOGGER.info("Executing query: insert product {} ", product.getName());

            String query = productQueries.getCreate();

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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

            Number key = keyHolder.getKey();
            if (key != null) {
                Long id = key.longValue();
                return findById(id);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            LOGGER.error("Error inserting product into database", e);
            throw new RepositoryException("Error inserting product into database", e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            LOGGER.info("Executing query: delete product with id: {}", id);
            int rowsAffected = jdbcTemplate.update(productQueries.getDeleteById(), id);
            return rowsAffected == 1;
        } catch (DataAccessException e) {
            LOGGER.error("Error deleting product from database", e);
            throw new RepositoryException("Error deleting product from database", e);
        }
    }

    @Override
    public Product updateById(Long id, Product product) {
        try {
            LOGGER.info("Executing query: update product by id: {}, product: {}", id, product);
            String query = productQueries.getUpdateById();
            KeyHolder keyHolder = new GeneratedKeyHolder();

            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setDouble(3, product.getPrice());
                if (product.getExpirationDate() != null) {
                    ps.setDate(4, Date.valueOf(product.getExpirationDate()));
                } else {
                    ps.setNull(4, Types.DATE);
                }
                ps.setLong(5,id);
                return ps;
            }, keyHolder);

            if(rowsAffected == 0) {
                return null;
            }
            return findById(id);
        } catch (DataAccessException e) {
            LOGGER.error("Error updating product into database", e);
            throw new RepositoryException("Error updating product into database", e);
        }
    }
}
