package com.example.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    private String description;

    @NonNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Positive
    private BigDecimal price;

    @NonNull
    @Size(min = 3, max = 3)
    private String currencyCode;
}
