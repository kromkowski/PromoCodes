package com.example.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    private Product product;

    @NonNull
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    @Positive
    private BigDecimal regularPrice;

    @Nullable
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    @DecimalMin(value = "0.00")
    private BigDecimal discountValue;

    @NonNull
    @Size(min=3, max=3)
    private String currencyCode;

    @CreatedDate
    private LocalDate purchaseDate;

}
