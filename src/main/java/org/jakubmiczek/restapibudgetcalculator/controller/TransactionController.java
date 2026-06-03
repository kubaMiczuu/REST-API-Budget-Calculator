package org.jakubmiczek.restapibudgetcalculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jakubmiczek.restapibudgetcalculator.dto.TransactionRequest;
import org.jakubmiczek.restapibudgetcalculator.dto.TransactionResponse;
import org.jakubmiczek.restapibudgetcalculator.model.TransactionCategory;
import org.jakubmiczek.restapibudgetcalculator.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionResponse> getTransaction(
            @RequestParam Long id,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) TransactionCategory category
    ) {
        return transactionService.findTransactions(id, from, to, category);
    }

    @PostMapping
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        transactionService.addTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
