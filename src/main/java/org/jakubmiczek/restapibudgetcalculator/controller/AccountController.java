package org.jakubmiczek.restapibudgetcalculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jakubmiczek.restapibudgetcalculator.dto.AccountRequest;
import org.jakubmiczek.restapibudgetcalculator.dto.AccountResponse;
import org.jakubmiczek.restapibudgetcalculator.model.Account;
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
    public ResponseEntity<Void> deleteAccount(@RequestParam Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
