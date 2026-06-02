package org.jakubmiczek.restapibudgetcalculator.repository;

import org.jakubmiczek.restapibudgetcalculator.model.Transaction;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TransactionSpecification {

    public static Specification<Transaction> hasAccountId(Long accountId) {
        return (root, query, cb) -> cb.equal(root.get("account").get("id"), accountId);
    }

    public static Specification<Transaction> dateFrom(LocalDate from) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), from);
    }

    public static Specification<Transaction> dateTo(LocalDate to) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), to);
    }

    public static Specification<Transaction> hasCategory(TransactionCategory category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }
}
