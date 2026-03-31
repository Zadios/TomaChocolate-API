package com.tomachocolate.api.controller;

import com.tomachocolate.api.dto.ExpenseRequest;
import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
