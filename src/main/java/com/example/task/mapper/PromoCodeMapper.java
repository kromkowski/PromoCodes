package com.example.task.mapper;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.model.PromoCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

public class PromoCodeMapper {
    public static PromoCode toPromoCode(PromoCodeDTO promoCodeDTO ) {
        return PromoCode.builder()
                .code(promoCodeDTO.getCode())
                .discount(promoCodeDTO.getDiscount())
                .currencyCode(promoCodeDTO.getCurrencyCode())
                .remainingUsages(promoCodeDTO.getMaxUsages())
                .expirationDate(promoCodeDTO.getExpirationDate())
                .build();
    }

    public static String toJson(PromoCodeDTO promoCodeDTO) {
        try {
            return "{\"code\":\"" + promoCodeDTO.getCode() + "\",\"discount\":" + promoCodeDTO.getDiscount() + ",\"currencyCode\":\"" + promoCodeDTO.getCurrencyCode() + "\",\"maxUsages\":" + promoCodeDTO.getMaxUsages() + ",\"expirationDate\":\"" + promoCodeDTO.getExpirationDate() + "\"}";
        } catch (Exception e) {
            throw new RuntimeException("Error converting promo code to JSON");
        }
    }

    public static PromoCode fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            objectMapper.setDateFormat(dateFormat);

            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(json, PromoCode.class);

        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to promo code");
        }
    }
}
