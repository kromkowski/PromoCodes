package com.example.task.DTO;

import jakarta.validation.constraints.Size;
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
public class ProductDTO {
    @NonNull
    private String name;

    private String description;

    @NonNull
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private BigDecimal price;

    @NonNull
    @Size(min=3, max=3)
    private String currencyCode;
}
