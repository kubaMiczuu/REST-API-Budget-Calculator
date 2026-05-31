package org.jakubmiczek.restapibudgetcalculator.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal amount, BigDecimal balance) {
        super("Balance was "+balance+" and was exceeded with amount of"+amount);
    }
}
