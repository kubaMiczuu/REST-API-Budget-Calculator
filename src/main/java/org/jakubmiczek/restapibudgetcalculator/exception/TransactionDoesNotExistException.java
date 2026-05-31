package org.jakubmiczek.restapibudgetcalculator.exception;

public class TransactionDoesNotExistException extends TransactionException {
    public TransactionDoesNotExistException(Long id) {
        super("Transaction with id " + id + " does not exist");
    }
}
