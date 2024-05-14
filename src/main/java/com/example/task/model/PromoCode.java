package com.example.task.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.NonNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "promocodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @NonNull
    @Size(min=3, max=24)
    private String code;

    @NonNull
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
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
