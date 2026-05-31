package org.jakubmiczek.restapibudgetcalculator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
    @Positive
    @NotNull
    BigDecimal amount,

    @NotNull
    TransactionType type,

    @NotNull
    TransactionCategory category,

    String description,

    @NotNull
    LocalDate date,

    @NotNull
    Long accountId
    )
{}
