package org.jakubmiczek.restapibudgetcalculator.service;

import org.jakubmiczek.restapibudgetcalculator.dto.TransactionRequest;
import org.jakubmiczek.restapibudgetcalculator.dto.TransactionResponse;
import org.jakubmiczek.restapibudgetcalculator.exception.AccountDoesNotExistException;
import org.jakubmiczek.restapibudgetcalculator.exception.InsufficientFundsException;
import org.jakubmiczek.restapibudgetcalculator.exception.TransactionDoesNotExistException;
import org.jakubmiczek.restapibudgetcalculator.model.Account;
import org.jakubmiczek.restapibudgetcalculator.model.Transaction;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionType;
import org.jakubmiczek.restapibudgetcalculator.repository.AccountRepository;
import org.jakubmiczek.restapibudgetcalculator.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldAddTransactionCorrectly() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("100.00"));

        LocalDate date = LocalDate.now();
        TransactionRequest transactionRequest = new TransactionRequest(BigDecimal.valueOf(500), TransactionType.INCOME, TransactionCategory.OTHER, null, date, 1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        transactionService.addTransaction(transactionRequest);
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction transaction = captor.getValue();

        assertThat(transaction.getAmount()).isEqualTo(BigDecimal.valueOf(500));
        assertThat(transaction.getCategory()).isEqualTo(TransactionCategory.OTHER);
        assertThat(transaction.getType()).isEqualTo(TransactionType.INCOME);
        assertThat(transaction.getDate()).isEqualTo(date);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExistWhenAddingTransaction() {
        TransactionRequest transactionRequest = new TransactionRequest(BigDecimal.valueOf(500), TransactionType.INCOME, TransactionCategory.OTHER, null, LocalDate.now(), 1L);
        assertThatThrownBy(() -> transactionService.addTransaction(transactionRequest))
                .isInstanceOf(AccountDoesNotExistException.class);
    }

    @Test
    void shouldDeleteTransactionCorrectly() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("200.00"));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.INCOME);
        transaction.setCategory(TransactionCategory.OTHER);
        transaction.setAccount(account);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        transactionService.deleteTransaction(1L);

        verify(transactionRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTransactionDoesNotExistWhenDeletingTransaction() {
        assertThatThrownBy(() -> transactionService.deleteTransaction(1L))
                .isInstanceOf(TransactionDoesNotExistException.class);
    }

    @Test
    void shouldRecalculateBalanceCorrectlyWhenAddingWithExpense() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("700.00"));

        TransactionRequest transactionRequest = new TransactionRequest(BigDecimal.valueOf(500), TransactionType.EXPENSE, TransactionCategory.OTHER, null, LocalDate.now(), 1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        transactionService.addTransaction(transactionRequest);

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(200));
    }

    @Test
    void shouldRecalculateBalanceCorrectlyWhenAddingWithIncome() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("700.00"));

        TransactionRequest transactionRequest = new TransactionRequest(BigDecimal.valueOf(500), TransactionType.INCOME, TransactionCategory.OTHER, null, LocalDate.now(), 1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        transactionService.addTransaction(transactionRequest);

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1200));
    }

    @Test
    void shouldRecalculateBalanceCorrectlyWhenDeletingWithExpense() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("700.00"));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setType(TransactionType.EXPENSE);
        transaction.setCategory(TransactionCategory.OTHER);
        transaction.setAccount(account);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        transactionService.deleteTransaction(1L);

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1200));
    }

    @Test
    void shouldRecalculateBalanceCorrectlyWhenDeletingWithIncome() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("700.00"));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setType(TransactionType.INCOME);
        transaction.setCategory(TransactionCategory.OTHER);
        transaction.setAccount(account);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        transactionService.deleteTransaction(1L);

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(200));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("200.00"));

        TransactionRequest transactionRequest = new TransactionRequest(BigDecimal.valueOf(500), TransactionType.EXPENSE, TransactionCategory.OTHER, null, LocalDate.now(), 1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.addTransaction(transactionRequest))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldReturnCorrectTransactionWithAllDataSet() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("200.00"));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setType(TransactionType.EXPENSE);
        transaction.setCategory(TransactionCategory.OTHER);
        transaction.setDate(LocalDate.now().minusDays(1));
        transaction.setAccount(account);

        LocalDate from = LocalDate.now().minusDays(2);
        LocalDate to = LocalDate.now();
        TransactionCategory category = TransactionCategory.OTHER;

        when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of(transaction));

        List<TransactionResponse> foundTransaction = transactionService.findTransactions(1L, from, to, category);

        assertThat(foundTransaction.getFirst().date()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(foundTransaction.getFirst().category()).isEqualTo(category);

    }

    @Test
    void shouldReturnCorrectTransactionWithOnlyId() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("200.00"));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("500.00"));
        transaction.setType(TransactionType.EXPENSE);
        transaction.setCategory(TransactionCategory.OTHER);
        transaction.setDate(LocalDate.now().minusDays(1));
        transaction.setAccount(account);

        when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of(transaction));

        List<TransactionResponse> foundTransaction = transactionService.findTransactions(1L, null, null, null);

        assertThat(foundTransaction.getFirst().accountId()).isEqualTo(1L);

    }
}
