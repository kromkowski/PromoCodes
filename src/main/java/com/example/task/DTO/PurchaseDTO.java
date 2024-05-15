package com.example.task.DTO;

import com.example.task.model.Product;
import com.example.task.model.PromoCode;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
    @NonNull
    private Product product;

    @Nullable
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    @DecimalMin(value = "0", inclusive = true)
    private BigDecimal discountPrice;

}
