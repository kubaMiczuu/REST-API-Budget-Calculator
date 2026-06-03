package org.jakubmiczek.restapibudgetcalculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jakubmiczek.restapibudgetcalculator.dto.AccountRequest;
import org.jakubmiczek.restapibudgetcalculator.dto.AccountResponse;
import org.jakubmiczek.restapibudgetcalculator.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountResponse> getAccounts() {
        return accountService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> addAccount(@Valid @RequestBody AccountRequest accountRequest) {
        accountService.addAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountDetails(@PathVariable Long id) {
        return accountService.getAccountDetails(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/transactions/export")
    public ResponseEntity<String> exportAccountTransactions(@PathVariable Long id) {
        String csvContent = accountService.exportTransactionsToCsv(id);
        String filename = "account_" + id + "_transactions.csv";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .contentType(org.springframework.http.MediaType.parseMediaType("text/csv"))
                .body(csvContent);
    }
}
