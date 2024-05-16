package com.example.task.controller;

import com.example.task.DTO.ProductDTO;
import com.example.task.DTO.PromoCodeDTO;
import com.example.task.model.Purchase;
import com.example.task.repository.ProductRepository;
import com.example.task.repository.PromoCodeRepository;
import com.example.task.repository.PurchaseRepository;
import com.example.task.service.ProductService;
import com.example.task.service.PromoCodeService;
import com.example.task.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
//@WebMvcTest(PromoCodeController.class)
@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromoCodeService promoCodeService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @BeforeEach
    public void cleanup() {
        purchaseRepository.deleteAll();
        productRepository.deleteAll();
        promoCodeRepository.deleteAll();
    }


    @Test
    void createPurchase() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("value", "TEST_CALCULATE", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
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

        ProductDTO productDTO = new ProductDTO("TEST", "descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        String requestBodyProduct = objectMapper.writeValueAsString(productDTO);
        response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyProduct))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        Long id = productRepository.findAll().getFirst().getId();

        response = mockMvc.perform(post("/api/purchases?productID=" + id + "&code=" + "TEST_CALCULATE"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        assertEquals("TEST", purchaseRepository.findAll().getFirst().getProduct().getName());
        assertEquals(BigDecimal.valueOf(22.22), purchaseRepository.findAll().getFirst().getRegularPrice());
        assertEquals(BigDecimal.valueOf(10.22), purchaseRepository.findAll().getFirst().getDiscountAmount());
        assertEquals("PLN", purchaseRepository.findAll().getFirst().getCurrencyCode());
        assertEquals(LocalDate.now(), purchaseRepository.findAll().getFirst().getPurchaseDate());
    }

    @Test
    void getAllPurchases() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("value", "TEST1", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
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

        ProductDTO productDTO = new ProductDTO("TEST", "descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        String requestBodyProduct = objectMapper.writeValueAsString(productDTO);
        response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyProduct))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        Long id = productRepository.findAll().getFirst().getId();

        response = mockMvc.perform(post("/api/purchases?productID=" + id + "&code=" + "TEST1"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());

        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/purchases"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        List<Purchase> purchases = objectMapper.readValue(response.getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Purchase.class));
        assertEquals(1, purchases.size());
        assertEquals("TEST", purchases.getFirst().getProduct().getName());
        assertEquals(BigDecimal.valueOf(22.22), purchases.getFirst().getRegularPrice());
        assertEquals(BigDecimal.valueOf(10.22), purchases.getFirst().getDiscountAmount());
        assertEquals("PLN", purchases.getFirst().getCurrencyCode());
        assertEquals(LocalDate.now(), purchases.getFirst().getPurchaseDate());
    }

    @Test
    void getPurchase() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("value", "TEST", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
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

        ProductDTO productDTO = new ProductDTO("TEST", "descrdsaption", BigDecimal.valueOf(22.22), "PLN");
        String requestBodyProduct = objectMapper.writeValueAsString(productDTO);
        response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyProduct))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        Long id = productRepository.findAll().getFirst().getId();

        response = mockMvc.perform(post("/api/purchases?productID=" + id + "&code=" + "TEST"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(201, response.getStatus());
        id = purchaseRepository.findAll().getFirst().getId();
        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/purchases/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        Purchase purchase = objectMapper.readValue(response.getContentAsString(), Purchase.class);
        assertEquals("TEST", purchase.getProduct().getName());
        assertEquals(BigDecimal.valueOf(22.22), purchase.getRegularPrice());
        assertEquals(BigDecimal.valueOf(10.22), purchase.getDiscountAmount());
        assertEquals("PLN", purchase.getCurrencyCode());
        assertEquals(LocalDate.now(), purchase.getPurchaseDate());
    }


}