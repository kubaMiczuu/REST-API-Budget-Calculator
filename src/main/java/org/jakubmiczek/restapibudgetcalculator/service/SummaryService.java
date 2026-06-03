package org.jakubmiczek.restapibudgetcalculator.service;

import org.jakubmiczek.restapibudgetcalculator.dto.SummaryResponse;
import org.jakubmiczek.restapibudgetcalculator.dto.TransactionResponse;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryService {

    private final TransactionService transactionService;

    public SummaryService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public SummaryResponse accountSummary(Long accountId) {
        List<TransactionResponse> transactions = transactionService.findTransactions(accountId, null, null, null);

        BigDecimal totalIncome = summarizedIncome(transactions);
        BigDecimal totalExpense = summarizedExpense(transactions);
        Map<TransactionCategory, BigDecimal> expensesByCategory = expensesByCategory(transactions);

        return new SummaryResponse(totalIncome, totalExpense, expensesByCategory);
    }

    private BigDecimal summarizedIncome(List<TransactionResponse> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.INCOME)
                .map(TransactionResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal summarizedExpense(List<TransactionResponse> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .map(TransactionResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<TransactionCategory, BigDecimal> expensesByCategory(List<TransactionResponse> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        TransactionResponse::category,
                        Collectors.mapping(
                                TransactionResponse::amount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }
}
