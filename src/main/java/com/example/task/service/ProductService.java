package com.example.task.service;

import com.example.task.DTO.ProductDTO;
import com.example.task.mapper.ProductMapper;
import com.example.task.model.Product;
import com.example.task.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductDTO productDTO) {
        try {
            Product product = ProductMapper.toProduct(productDTO);
            return productRepository.save(product);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bad request. Check the productDTO fields and try again."
            );
        }
    }

    public Product getProduct(Long id) {
        var product = productRepository.findFirstById(id);
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Product of such id was not found."
            );
        }
        return product;
    }

    public List<Product> getAllProducts() {
        var products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "No products found."
            );
        }
        return products;
    }

    public Product updateProduct(ProductDTO productDTO, Long id) {
        var product = getProduct(id);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCurrencyCode(productDTO.getCurrencyCode());
        return productRepository.save(product);
    }

}
