package org.jakubmiczek.restapibudgetcalculator.exception;

public class AccountDoesNotExistException extends RuntimeException {
    public AccountDoesNotExistException(Long id) {
        super("Account with id " + id + " does not exist");
    }
}
