package com.example.transaction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    private UUID id;

    @NotBlank
    private String type; // e.g., DEPOSIT, WITHDRAWAL

    @NotNull
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

    private String description;

    private LocalDateTime timestamp;
}