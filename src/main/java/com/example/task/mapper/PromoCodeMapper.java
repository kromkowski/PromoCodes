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
        return promoCode;
    }
}
