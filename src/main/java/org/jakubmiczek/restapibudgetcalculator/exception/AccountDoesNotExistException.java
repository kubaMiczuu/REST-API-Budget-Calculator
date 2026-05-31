package org.jakubmiczek.restapibudgetcalculator.exception;

public class AccountDoesNotExistException extends AccountException {
    public AccountDoesNotExistException(Long id) {
        super("Account with id " + id + " does not exist");
    }
}
