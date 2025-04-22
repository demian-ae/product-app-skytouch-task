package com.example.repository;

import com.example.common.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresProductRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ProductQueries productQueries;

    @InjectMocks
    private PostgresProductRepository repository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "A product", 9.99, LocalDate.now());
    }

    @Test
    void findAll_shouldReturnProducts() {
        String query = "SELECT * FROM products";
        when(productQueries.getGetAll()).thenReturn(query);
        when(jdbcTemplate.query(eq(query), any(RowMapper.class))).thenReturn(List.of(product));

        List<Product> result = repository.findAll();

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }

    @Test
    void save_shouldReturnProductWithGeneratedId() {
        String query = "INSERT INTO products ...";
        when(productQueries.getCreate()).thenReturn(query);

        // Mock INSERT behavior
        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Map.of("id", 10L)); // simulate DB returning generated ID
            return null;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // Mock findById call after insertion
        when(productQueries.getGetById()).thenReturn("SELECT * FROM products WHERE id = ?");
        when(jdbcTemplate.query(eq("SELECT * FROM products WHERE id = ?"), any(RowMapper.class), eq(10L)))
                .thenReturn(List.of(new Product(10L, "Test Product", "A product", 9.99, LocalDate.now())));

        Product result = repository.save(product);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Test Product", result.getName());
    }

    @Test
    void deleteById_shouldCallJdbcTemplateUpdate() {
        String query = "DELETE FROM products WHERE id = ?";
        when(productQueries.getDeleteById()).thenReturn(query);

        repository.deleteById(10L);

        verify(jdbcTemplate).update(query, 10L);
    }

    @Test
    void updateById_shouldReturnUpdatedProduct() {
        String query = "UPDATE products SET ...";
        when(productQueries.getUpdateById()).thenReturn(query);

        // Simulate successful update
        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Map.of("id", 10L));
            return 1; // rows affected
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // Mock findById call after update
        when(productQueries.getGetById()).thenReturn("SELECT * FROM products WHERE id = ?");
        when(jdbcTemplate.query(eq("SELECT * FROM products WHERE id = ?"), any(RowMapper.class), eq(10L)))
                .thenReturn(List.of(new Product(10L, "Updated Product", "Updated", 9.99, LocalDate.now())));

        Product result = repository.updateById(10L, product);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Updated Product", result.getName());
    }

    @Test
    void findById_shouldReturnProductWhenExists() {
        String query = "SELECT * FROM products WHERE id = ?";
        when(productQueries.getGetById()).thenReturn(query);
        when(jdbcTemplate.query(eq(query), any(RowMapper.class), eq(1L))).thenReturn(List.of(product));

        Product result = repository.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        String query = "SELECT * FROM products WHERE id = ?";
        when(productQueries.getGetById()).thenReturn(query);
        when(jdbcTemplate.query(eq(query), any(RowMapper.class), eq(1L))).thenReturn(Collections.emptyList());

        Product result = repository.findById(1L);

        assertNull(result);
    }

    @Test
    void findById_shouldThrowRepositoryExceptionOnDataAccessException() {
        String query = "SELECT * FROM products WHERE id = ?";
        when(productQueries.getGetById()).thenReturn(query);
        when(jdbcTemplate.query(eq(query), any(RowMapper.class), eq(1L)))
                .thenThrow(new DataAccessException("DB error") {});

        assertThrows(RepositoryException.class, () -> repository.findById(1L));
    }
}