package com.tomachocolate.api.controller;

import com.tomachocolate.api.dto.ExpenseRequest;
import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/{meetingId}/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public Expense createExpense(
            @PathVariable UUID meetingId,
            @Valid @RequestBody ExpenseRequest request
    ) {
        return expenseService.addExpense(meetingId, request);
    }

    @DeleteMapping("/{meetingId}/expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable UUID meetingId,
            @PathVariable Long expenseId) {

        expenseService.deleteExpense(meetingId, expenseId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseRequest request) {

        expenseService.updateExpense(expenseId, request);
    }
}
