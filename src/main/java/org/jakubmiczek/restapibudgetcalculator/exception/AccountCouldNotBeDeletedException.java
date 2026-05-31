package org.jakubmiczek.restapibudgetcalculator.exception;

public class AccountCouldNotBeDeletedException extends RuntimeException {
    public AccountCouldNotBeDeletedException(Long id) {
        super("Account with id " + id + " could not be deleted because It has some transactions left");
    }
}
