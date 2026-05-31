package org.jakubmiczek.restapibudgetcalculator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    Long id;

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "balance", nullable = false)
    BigDecimal balance;
}
