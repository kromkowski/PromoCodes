package com.example.task.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeDTO {
    @NonNull
    @Size(min=3, max=24)
    private String code;

    @NonNull
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    @Positive
    private BigDecimal discount;

    @NonNull
    @Size(min=3, max=3)
    private String currencyCode;

    @NonNull
    @Min(1)
    private Integer maxUsages;

    @NonNull
    private LocalDate expirationDate;
}
