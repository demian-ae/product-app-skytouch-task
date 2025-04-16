package com.example.service;

import com.example.common.Product;
import com.example.common.ProductRequest;
import com.example.common.ProductResponse;
import com.example.common.ProductResponseStatus;
import com.example.repository.ProductRepository;
import com.example.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse handleRequest(ProductRequest request) {
        if (request.getAction() == null) {
            throw new IllegalArgumentException("ProductRequest action must not be null");
        }

        try {
            switch (request.getAction()) {
                case GET_ALL:
                    List<Product> all = findAll();
                    return new ProductResponse(ProductResponseStatus.OK, all);

                case CREATE:
                    Product created = save(request.getProduct());
                    if(created != null){
                        return new ProductResponse(ProductResponseStatus.CREATED, List.of(created));
                    } else {
                        return new ProductResponse(ProductResponseStatus.INTERNAL_SERVER_ERROR, Collections.emptyList());
                    }

                case DELETE_BY_ID:
                    boolean isDeleted = deleteById(request.getProductId());
                    return new ProductResponse(isDeleted? ProductResponseStatus.OK : ProductResponseStatus.NOT_FOUND, Collections.emptyList());

                case UPDATE_BY_ID:
                    Product updated = updateById(request.getProductId(), request.getProduct());
                    if(updated != null) {
                        return new ProductResponse(ProductResponseStatus.OK, List.of(updated));
                    } else {
                        return new ProductResponse(ProductResponseStatus.NOT_FOUND, Collections.emptyList());
                    }

                default:
                    throw new IllegalArgumentException("Unsupported request type: " + request.getAction());
            }
        } catch (RepositoryException e) {
            LOGGER.error("Repository error", e);
            return new ProductResponse(ProductResponseStatus.INTERNAL_SERVER_ERROR, Collections.emptyList());
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
            return new ProductResponse(ProductResponseStatus.INTERNAL_SERVER_ERROR, Collections.emptyList());
        }
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateById(Long id, Product product) {
        return productRepository.updateById(id, product);
    }

    @Override
    public boolean deleteById(Long id) {
        return productRepository.deleteById(id);
    }
}
