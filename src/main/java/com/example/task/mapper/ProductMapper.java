package com.example.task.mapper;

import com.example.task.DTO.ProductDTO;
import com.example.task.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductMapper {
    public static Product toProduct(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .currencyCode(productDTO.getCurrencyCode())
                .build();
    }

    public static String toJson(ProductDTO productDTO) {
        try {
            return "{\"name\":\"" + productDTO.getName() + "\",\"description\":\"" + productDTO.getDescription() + "\",\"price\":" + productDTO.getPrice() + ",\"currencyCode\":\"" + productDTO.getCurrencyCode() + "\"}";
        } catch (Exception e) {
            throw new RuntimeException("Error converting product to JSON");
        }
    }

    public static Product fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, Product.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to product");
        }
    }
}
