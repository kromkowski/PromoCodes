package com.example.task.controller;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.mapper.PromoCodeMapper;
import com.example.task.model.Product;
import com.example.task.model.PromoCode;
import com.example.task.model.PromoCodePercentage;
import com.example.task.model.PromoCodeValue;
import com.example.task.repository.PromoCodeRepository;
import com.example.task.service.PromoCodeService;
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
class PromoCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromoCodeService promoCodeService;

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @AfterEach
    public void cleanup() {
        promoCodeRepository.deleteAll();
    }

    @Test
    void createPromoCodeValue() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("value","TEST", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
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
        assertEquals("TEST", promoCodeRepository.findFirstByCode("TEST").getCode());
        assertEquals(promoCodeDTO.getCurrencyCode(), promoCodeRepository.findFirstByCode("TEST").getCurrencyCode());
        assertEquals(promoCodeDTO.getMaxUsages(), promoCodeRepository.findFirstByCode("TEST").getRemainingUses());
        assertEquals(promoCodeDTO.getExpirationDate(), promoCodeRepository.findFirstByCode("TEST").getExpirationDate());
    }

    @Test
    void createPromoCodePercentage() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("percentage","TEST", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
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
        assertEquals("TEST", promoCodeRepository.findFirstByCode("TEST").getCode());
        assertEquals(promoCodeDTO.getCurrencyCode(), promoCodeRepository.findFirstByCode("TEST").getCurrencyCode());
        assertEquals(promoCodeDTO.getMaxUsages(), promoCodeRepository.findFirstByCode("TEST").getRemainingUses());
        assertEquals(promoCodeDTO.getExpirationDate(), promoCodeRepository.findFirstByCode("TEST").getExpirationDate());
    }


    @Test
    void getAllPromoCodes() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("percentage","TEST", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
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


        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/promo-codes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        List<PromoCode> list = objectMapper.readValue(response.getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, PromoCodePercentage.class));
        assertEquals(1, list.size());
        assertEquals("TEST", list.get(0).getCode());
        assertEquals("PLN", list.get(0).getCurrencyCode());
        assertEquals(3, list.get(0).getRemainingUses());
        assertEquals(promoCodeDTO.getExpirationDate(), list.get(0).getExpirationDate());



    }

    @Test
    void getPromoCode() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("percentage", "TEST", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
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

        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/promo-codes/TEST"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());
        PromoCode promoCode = objectMapper.readValue(response.getContentAsString(), PromoCodePercentage.class);
        assertEquals("TEST", promoCode.getCode());
        assertEquals("PLN", promoCode.getCurrencyCode());
        assertEquals(3, promoCode.getRemainingUses());
        assertEquals(promoCodeDTO.getExpirationDate(), promoCode.getExpirationDate());

    }
}