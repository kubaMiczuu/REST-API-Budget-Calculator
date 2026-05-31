package org.jakubmiczek.restapibudgetcalculator.dto;

import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long transactionId,
        BigDecimal amount,
        TransactionType type,
        TransactionCategory category,
        String description,
        LocalDate date,
        Long accountId) {
}
