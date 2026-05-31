package org.jakubmiczek.restapibudgetcalculator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    Long id;

    @Column(name = "amount", nullable = false)
    BigDecimal amount;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    TransactionType type;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    TransactionCategory category;

    @Column(name = "description")
    String description;

    @Column(name = "date", nullable = false)
    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "account_id",  nullable = false)
    Account account;
}
