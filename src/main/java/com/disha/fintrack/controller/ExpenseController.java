package com.disha.fintrack.controller;

import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO saved = expenseService.addExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        ExpenseDTO expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByProfileId(@PathVariable Long profileId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByProfileId(profileId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCategory(@PathVariable Long categoryId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByCategory(categoryId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllUserExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllUserExpenses();
        return ResponseEntity.ok(expenses);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO updated = expenseService.updateExpense(id, expenseDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current-month")
    public ResponseEntity<List<ExpenseDTO>> getCurrentMonthExpensesForCurrentUser() {
        List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/latest-five")
    public ResponseEntity<List<ExpenseDTO>> getFiveLatestExpense() {
        List<ExpenseDTO> expenses = expenseService.getFiveLatestExpense();
        return ResponseEntity.ok(expenses);
    }
}

