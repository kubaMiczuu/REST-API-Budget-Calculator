package org.jakubmiczek.restapibudgetcalculator.service;

import org.jakubmiczek.restapibudgetcalculator.dto.AccountRequest;
import org.jakubmiczek.restapibudgetcalculator.dto.AccountResponse;
import org.jakubmiczek.restapibudgetcalculator.exception.AccountCouldNotBeDeletedException;
import org.jakubmiczek.restapibudgetcalculator.exception.AccountDoesNotExistException;
import org.jakubmiczek.restapibudgetcalculator.model.Account;
import org.jakubmiczek.restapibudgetcalculator.model.Transaction;
import org.jakubmiczek.restapibudgetcalculator.repository.AccountRepository;
import org.jakubmiczek.restapibudgetcalculator.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    AccountService(AccountRepository accountRepository,  TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountDoesNotExistException(id));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public void addAccount(AccountRequest requestedAccount) {
        Account newAccount = new Account();
        newAccount.setName(requestedAccount.name());
        newAccount.setBalance(BigDecimal.ZERO);
        accountRepository.save(newAccount);
    }

    public void deleteAccount(Long id) {
        List<Transaction> transactions = transactionRepository.findAll().stream().filter(t -> t.getAccount().getId().equals(id)).toList();

        if(accountRepository.findById(id).isEmpty()) throw new AccountDoesNotExistException(id);
        else if(!transactions.isEmpty()) throw new AccountCouldNotBeDeletedException(id);
        else accountRepository.deleteById(id);
    }

    public AccountResponse getAccountDetails(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountDoesNotExistException(id));

        return new AccountResponse(account.getId(),  account.getName(), account.getBalance());
    }
}
