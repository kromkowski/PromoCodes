package com.example.task.mapper;

import com.example.task.DTO.PurchaseDTO;
import com.example.task.model.Purchase;

import java.time.LocalDate;

public class PurchaseMapper {
    public static Purchase toPurchase(PurchaseDTO purchaseDTO) {
        return Purchase.builder()
                .product(purchaseDTO.getProduct())
                .regularPrice(purchaseDTO.getProduct().getPrice())
                .discountPrice(purchaseDTO.getDiscountPrice())
                .currencyCode(purchaseDTO.getProduct().getCurrencyCode())
                .purchaseDate(LocalDate.now()).build();
    }

}
