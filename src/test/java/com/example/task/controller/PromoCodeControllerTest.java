package com.example.task.controller;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.TaskApplication;
import com.example.task.mapper.PromoCodeMapper;
import com.example.task.model.PromoCode;
import com.example.task.repository.PromoCodeRepository;
import com.example.task.service.PromoCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

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
    void createPromoCode() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("TEST", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
        String requestBody = PromoCodeMapper.toJson(promoCodeDTO);

        var result = mockMvc.perform(post("/api/promocodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        PromoCode promoCode = PromoCodeMapper.fromJson(responseBody);
        assertEquals(promoCodeDTO.getCode(), promoCode.getCode());
        assertEquals(promoCodeDTO.getDiscount(), promoCode.getDiscount());
        assertEquals(promoCodeDTO.getCurrencyCode(), promoCode.getCurrencyCode());
        assertEquals(promoCodeDTO.getMaxUsages(), promoCode.getMaxUsages());
        assertEquals(promoCodeDTO.getExpirationDate(), promoCode.getExpirationDate());
    }


    @Test
    void getAllPromoCodes() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("TEST", BigDecimal.valueOf(10.62), "PLN", 3, LocalDate.of(2024, 7, 5));
        PromoCodeDTO promoCodeDTO2 = new PromoCodeDTO("TEST2", BigDecimal.valueOf(20.22), "PLN", 3, LocalDate.of(2024, 7, 5));

        List<PromoCodeDTO> promoCodeDTOList = List.of(promoCodeDTO, promoCodeDTO2);
        List<PromoCode> promoCodeList = new ArrayList<>();

        for(PromoCodeDTO promoCodeDTOItem : promoCodeDTOList) {
            String requestBody = PromoCodeMapper.toJson(promoCodeDTOItem);
            var result = mockMvc.perform(post("/api/promocodes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn();
            String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
            promoCodeList.add(PromoCodeMapper.fromJson(responseBody));
        }
        var getResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/promocodes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseContent = getResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.registerModule(new JavaTimeModule());
        List<PromoCode> promoCodesGet = List.of(objectMapper.readValue(responseContent, PromoCode[].class));
        assertEquals(promoCodeList.size(), promoCodesGet.size());

        for(PromoCode promoCodeFromGet: promoCodesGet) {
            for(PromoCode promoCode : promoCodeList) {
                if(promoCodeFromGet.getCode().equals(promoCode.getCode())) {
                    assertEquals(promoCode.getCode(), promoCodeFromGet.getCode());
                    assertEquals(promoCode.getDiscount(), promoCodeFromGet.getDiscount());
                    assertEquals(promoCode.getCurrencyCode(), promoCodeFromGet.getCurrencyCode());
                    assertEquals(promoCode.getMaxUsages(), promoCodeFromGet.getMaxUsages());
                    assertEquals(promoCode.getExpirationDate(), promoCodeFromGet.getExpirationDate());
                }
            }
        }

    }

    @Test
    void getPromoCode() throws Exception {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO("TEST", BigDecimal.valueOf(10.22), "PLN", 3, LocalDate.of(2024, 7, 5));
        String requestBody = PromoCodeMapper.toJson(promoCodeDTO);

        var result = mockMvc.perform(post("/api/promocodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        PromoCode promoCode = PromoCodeMapper.fromJson(responseBody);
        var id = promoCode.getId();
        var getResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/promocodes/" + id))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String responseContent = getResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);
        objectMapper.registerModule(new JavaTimeModule());
        PromoCode promoCodeFromGet = objectMapper.readValue(responseContent, PromoCode.class);
        assertEquals(promoCode.getCode(), promoCodeFromGet.getCode());
        assertEquals(promoCode.getDiscount(), promoCodeFromGet.getDiscount());
        assertEquals(promoCode.getCurrencyCode(), promoCodeFromGet.getCurrencyCode());
        assertEquals(promoCode.getMaxUsages(), promoCodeFromGet.getMaxUsages());
        assertEquals(promoCode.getExpirationDate(), promoCodeFromGet.getExpirationDate());
    }
}