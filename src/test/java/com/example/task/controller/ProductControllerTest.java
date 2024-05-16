package com.example.task.controller;

import com.example.task.DTO.ProductDTO;
import com.example.task.DTO.PromoCodeDTO;
import com.example.task.model.Product;
import com.example.task.repository.ProductRepository;
import com.example.task.repository.PromoCodeRepository;
import com.example.task.service.ProductService;
import com.example.task.service.PromoCodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
//@WebMvcTest(PromoCodeController.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromoCodeService promoCodeService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
        promoCodeRepository.deleteAll();
    }

    @Test
    void createProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO("TEST","descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(productDTO);
        var response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        assertEquals(1, productRepository.count());
        assertEquals("TEST", productRepository.findAll().get(0).getName());
        assertEquals("descrdsaption", productRepository.findAll().get(0).getDescription());
        assertEquals(BigDecimal.valueOf(22.22), productRepository.findAll().get(0).getPrice());
        assertEquals("PLN", productRepository.findAll().get(0).getCurrencyCode());
    }

    @Test
    void getAllProducts() throws Exception {
        ProductDTO productDTO = new ProductDTO("TEST","descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(productDTO);
        var response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());

        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        List<Product> list = objectMapper.readValue(response.getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));
        assertEquals(1, list.size());
        assertEquals("TEST", list.get(0).getName());
        assertEquals("descrdsaption", list.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(22.22), list.get(0).getPrice());
        assertEquals("PLN", list.get(0).getCurrencyCode());
    }

    @Test
    void getProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO("TEST","descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(productDTO);
        var response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        Long id = productRepository.findAll().get(0).getId();
        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/"+id))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        Product product = objectMapper.readValue(response.getContentAsString(), Product.class);

        assertEquals("TEST", product.getName());
        assertEquals("descrdsaption", product.getDescription());
        assertEquals(BigDecimal.valueOf(22.22), product.getPrice());
        assertEquals("PLN", product.getCurrencyCode());
    }

    @Test
    void updateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO("TEST","descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(productDTO);
        var response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());

        ProductDTO productDTOEdited = new ProductDTO("TEST","no description", BigDecimal.valueOf(22.22), "PLN");
        String requestBodyEdited = objectMapper.writeValueAsString(productDTOEdited);
        response = mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyEdited))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertEquals("no description", productRepository.findFirstById(1L).getDescription());

    }

    @Test
    void calculateDiscountValue() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("value","TEST_CALCULATE", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(promoCodeDTO);

        var response = mockMvc.perform(post("/api/promo-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());

        ProductDTO productDTO = new ProductDTO("TEST","descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        String requestBodyProduct = objectMapper.writeValueAsString(productDTO);
        response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyProduct))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        Long id = productRepository.findAll().get(0).getId();
        System.out.println(promoCodeRepository.findAll().get(0));
        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/"+id+"/calculate-discount?code=TEST_CALCULATE"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("12.00"));
    }
}