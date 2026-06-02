package org.jakubmiczek.restapibudgetcalculator.service;

import jakarta.transaction.Transactional;
import org.jakubmiczek.restapibudgetcalculator.dto.TransactionRequest;
import org.jakubmiczek.restapibudgetcalculator.exception.AccountDoesNotExistException;
import org.jakubmiczek.restapibudgetcalculator.exception.InsufficientFundsException;
import org.jakubmiczek.restapibudgetcalculator.exception.TransactionDoesNotExistException;
import org.jakubmiczek.restapibudgetcalculator.model.Account;
import org.jakubmiczek.restapibudgetcalculator.model.Transaction;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionType;
import org.jakubmiczek.restapibudgetcalculator.repository.AccountRepository;
import org.jakubmiczek.restapibudgetcalculator.repository.TransactionRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.jakubmiczek.restapibudgetcalculator.repository.TransactionSpecification.*;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void addTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();

        Optional<Account> account = accountRepository.findById(transactionRequest.accountId());
        if(account.isEmpty()) throw new AccountDoesNotExistException(transactionRequest.accountId());

        transaction.setAmount(transactionRequest.amount());
        transaction.setType(transactionRequest.type());
        transaction.setDescription(transactionRequest.description());
        transaction.setCategory(transactionRequest.category());
        transaction.setAccount(account.get());
        transaction.setDate(transactionRequest.date());

        transactionRepository.save(transaction);

        recalculateAfterAddition(account.get(), transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new TransactionDoesNotExistException(id));

        transactionRepository.deleteById(id);
        recalculateAfterDeletion(transaction.getAccount(),  transaction);
    }

    private void recalculateAfterDeletion(Account account, Transaction transaction) {
        BigDecimal balance = account.getBalance();
        BigDecimal amount = transaction.getAmount();
        TransactionType type = transaction.getType();

        if(type.equals(TransactionType.EXPENSE)) account.setBalance(balance.add(amount));
        else account.setBalance(balance.subtract(amount));

        accountRepository.save(account);
    }

    private void recalculateAfterAddition(Account account, Transaction transaction) {
        BigDecimal balance = account.getBalance();
        BigDecimal amount = transaction.getAmount();
        TransactionType type = transaction.getType();


        if(type.equals(TransactionType.EXPENSE)) {
            if(balance.compareTo(amount) < 0) throw new InsufficientFundsException(amount, balance);
            account.setBalance(balance.subtract(amount));
        }
        else account.setBalance(balance.add(amount));

        accountRepository.save(account);
    }

    public List<Transaction> findTransactions(Long id, LocalDate from, LocalDate to, TransactionCategory category) {
        Specification<Transaction> spec = Specification.where(hasAccountId(id));
        if (from != null) spec = spec.and(dateFrom(from));
        if (to != null) spec = spec.and(dateTo(to));
        if (category != null) spec = spec.and(hasCategory(category));

        return transactionRepository.findAll(spec);
    }
}
