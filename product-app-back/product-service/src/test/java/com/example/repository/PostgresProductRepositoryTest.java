package com.example.repository;

import com.example.common.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDate;
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

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        keyHolder.getKeyList().add(Map.of("id", 10L));

        doAnswer(invocation -> {
            PreparedStatementCreator psc = invocation.getArgument(0);
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Map.of("id", 10L));
            return null;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        Product result = repository.save(product);

        assertNotNull(result);
        assertEquals(10L, result.getId());
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

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        keyHolder.getKeyList().add(Map.of("id", 10L));

        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Map.of("id", 10L));
            return null;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        Product result = repository.updateById(10L, product);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }
}