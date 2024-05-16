package com.example.task.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;


@Entity
@Table(name = "promocodes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DiscriminatorColumn(name = "promo-code_type",
        discriminatorType = DiscriminatorType.STRING)
public class PromoCode {

    @Id
    @NonNull
    @Size(min = 3, max = 24)
    private String code;

    @NonNull
    @Size(min = 3, max = 3)
    private String currencyCode;

    @NonNull
    @Min(0)
    private Integer remainingUses;

    @NonNull
    private LocalDate expirationDate;
}
