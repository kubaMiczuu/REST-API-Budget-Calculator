package org.jakubmiczek.restapibudgetcalculator.exception;

import javax.security.auth.login.AccountNotFoundException;

public class AccountCouldNotBeDeletedException extends AccountException {
    public AccountCouldNotBeDeletedException(Long id) {
        super("Account with id " + id + " could not be deleted because It has some transactions left");
    }
}
