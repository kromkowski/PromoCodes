package com.example.task.mapper;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.model.PromoCode;

public class PromoCodeMapper {
    public static PromoCode toPromoCode(PromoCodeDTO promoCodeDTO ) {
        return PromoCode.builder()
                .code(promoCodeDTO.getCode())
                .discount(promoCodeDTO.getDiscount())
                .currencyCode(promoCodeDTO.getCurrencyCode())
                .maxUsages(promoCodeDTO.getMaxUsages())
                .expirationDate(promoCodeDTO.getExpirationDate())
                .build();
    }
}
