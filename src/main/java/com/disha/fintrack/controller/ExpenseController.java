package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
@CrossOrigin("*")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO saved = expenseService.addExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved, "Expense created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getExpenseById(@PathVariable Long id) {
        ExpenseDTO expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(ApiResponse.success(expense));
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByProfileId(@PathVariable Long profileId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByProfileId(profileId);
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpensesByCategory(@PathVariable Long categoryId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpense() {
        return ResponseEntity.ok(ApiResponse.success(expenseService.getTotalExpenseForCurrentUser()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllUserExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllUserExpenses();
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(@PathVariable Long id,
                                                                 @RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO updated = expenseService.updateExpense(id, expenseDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Expense updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Expense deleted successfully"));
    }

    @GetMapping("/current-month")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getCurrentMonthExpensesForCurrentUser() {
        List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    @GetMapping("/latest-five")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getFiveLatestExpense() {
        List<ExpenseDTO> expenses = expenseService.getFiveLatestExpense();
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    @GetMapping("/month")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpenseForMonth(@RequestParam int month,
                                                                            @RequestParam int year) {
        List<ExpenseDTO> expenses = expenseService.getExpenseForMonth(month, year);
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }
}
