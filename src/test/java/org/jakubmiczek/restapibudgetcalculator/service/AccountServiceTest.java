    package org.jakubmiczek.restapibudgetcalculator.service;

    import org.jakubmiczek.restapibudgetcalculator.dto.AccountRequest;
    import org.jakubmiczek.restapibudgetcalculator.dto.AccountResponse;
    import org.jakubmiczek.restapibudgetcalculator.exception.AccountCouldNotBeDeletedException;
    import org.jakubmiczek.restapibudgetcalculator.exception.AccountDoesNotExistException;
    import org.jakubmiczek.restapibudgetcalculator.model.Account;
    import org.jakubmiczek.restapibudgetcalculator.model.Transaction;
    import org.jakubmiczek.restapibudgetcalculator.repository.AccountRepository;
    import org.jakubmiczek.restapibudgetcalculator.repository.TransactionRepository;
    import static org.assertj.core.api.AssertionsForClassTypes.*;

    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.ArgumentCaptor;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;

    import java.math.BigDecimal;
    import java.util.List;
    import java.util.Optional;

    import static org.mockito.Mockito.*;


    @ExtendWith(MockitoExtension.class)
    public class AccountServiceTest {

        @Mock
        AccountRepository accountRepository;

        @Mock
        TransactionRepository transactionRepository;

        @InjectMocks
        AccountService accountService;

        @Test
        void shouldAddAccountCorrectly() {
            AccountRequest newAccount = new AccountRequest("Savings");
            accountService.addAccount(newAccount);

            ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository).save(captor.capture());
            Account account = captor.getValue();

            assertThat(account.getName()).isEqualTo("Savings");
            assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void shouldDeleteAccountCorrectly() {
            AccountRequest newAccount = new AccountRequest("Savings");
            accountService.addAccount(newAccount);

            ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository).save(captor.capture());
            Account account = captor.getValue();

            Long id = 1L;
            when(accountRepository.findById(id)).thenReturn(Optional.of(account));
            accountService.deleteAccount(id);

            verify(accountRepository).deleteById(id);
        }

        @Test
        void shouldThrowExceptionWhenAccountDoesNotExistWhenDeletingAccount() {
            when(accountRepository.findById(5L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> accountService.deleteAccount(5L))
                    .isInstanceOf(AccountDoesNotExistException.class);
        }

        @Test
        void shouldThrowExceptionWhenAccountHasSomeTransactionsWhenDeletingAccount() {
            Account account = new Account();
            account.setId(1L);

            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

            Transaction transaction = new Transaction();
            transaction.setAccount(account);

            when(transactionRepository.findAll()).thenReturn(List.of(transaction));

            assertThatThrownBy(() -> accountService.deleteAccount(1L))
                    .isInstanceOf(AccountCouldNotBeDeletedException.class);
        }

        @Test
        void shouldThrowExceptionWhenAccountWithIdDoesNotExist() {
            assertThatThrownBy(() -> accountService.findById(5L))
                    .isInstanceOf(AccountDoesNotExistException.class);
        }

        @Test
        void shouldReturnAllAccounts() {
            Account account = new Account();
            account.setId(1L);

            when(accountRepository.findAll()).thenReturn(List.of(account));

            List<AccountResponse> accounts = accountService.findAll();

            assertThat(accounts.size()).isEqualTo(1);
        }

        @Test
        void shouldThrowExceptionWhenAccountDoesNotExistWhenGettingAccountDetails() {
            assertThatThrownBy(() -> accountService.getAccountDetails(5L))
                    .isInstanceOf(AccountDoesNotExistException.class);
        }

        @Test
        void shouldReturnCorrectAccountDetails() {
            Account account = new Account();
            account.setId(1L);
            account.setName("Savings");
            account.setBalance(BigDecimal.ZERO);

            when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

            AccountResponse res = accountService.getAccountDetails(1L);

            assertThat(res.accountId()).isEqualTo(1L);
            assertThat(res.name()).isEqualTo("Savings");
            assertThat(res.balance()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }
