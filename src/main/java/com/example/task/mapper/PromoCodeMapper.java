package com.example.task.mapper;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.model.PromoCode;
import com.example.task.model.PromoCodePercentage;
import com.example.task.model.PromoCodeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;

public class PromoCodeMapper {
    public static PromoCode toPromoCode(PromoCodeDTO promoCodeDTO) {
        PromoCode promoCode;
        if (promoCodeDTO.getPromoCodeType().equals("percentage")) {
            promoCode = new PromoCodePercentage(promoCodeDTO.getDiscount());
            promoCode.setCode(promoCodeDTO.getCode().toUpperCase());
            promoCode.setCurrencyCode(promoCodeDTO.getCurrencyCode());
            promoCode.setRemainingUses(promoCodeDTO.getMaxUsages());
            promoCode.setExpirationDate(promoCodeDTO.getExpirationDate());
        } else if (promoCodeDTO.getPromoCodeType().equals("value")) {
            promoCode = new PromoCodeValue(promoCodeDTO.getDiscount());
            promoCode.setCode(promoCodeDTO.getCode());
            promoCode.setCurrencyCode(promoCodeDTO.getCurrencyCode());
            promoCode.setRemainingUses(promoCodeDTO.getMaxUsages());
            promoCode.setExpirationDate(promoCodeDTO.getExpirationDate());
        } else {
            throw new RuntimeException("Invalid promo code type");
        }
//        return PromoCode.builder()
//                .code(promoCodeDTO.getCode())
//                .discount(promoCodeDTO.getDiscount())
//                .currencyCode(promoCodeDTO.getCurrencyCode())
//                .remainingUses(promoCodeDTO.getMaxUsages())
//                .expirationDate(promoCodeDTO.getExpirationDate())
//                .build();
        return promoCode;
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
