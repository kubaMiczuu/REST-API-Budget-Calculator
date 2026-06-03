package org.jakubmiczek.restapibudgetcalculator.dto;

import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;

import java.math.BigDecimal;
import java.util.Map;

public record SummaryResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        Map<TransactionCategory, BigDecimal> expensesByCategory
) {
}
