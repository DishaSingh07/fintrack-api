package com.disha.fintrack.controller;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.BudgetRequest;
import com.disha.fintrack.dto.BudgetResponse;
import com.disha.fintrack.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ApiResponse<List<BudgetResponse>> getBudgets(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        return ApiResponse.success(
                budgetService.getAllBudgets(month, year)
        );
    }

    @GetMapping("/{budgetId}")
    public ApiResponse<BudgetResponse> getBudget(
            @PathVariable Long budgetId
    ) {
        return ApiResponse.success(
                budgetService.getBudget(budgetId)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BudgetResponse> createBudget(
            @RequestBody @Valid BudgetRequest request
    ) {
        return ApiResponse.success(
                budgetService.createBudget(request)
        );
    }

    @PutMapping("/{budgetId}")
    public ApiResponse<BudgetResponse> updateBudget(
            @PathVariable Long budgetId,
            @RequestBody BudgetRequest request
    ) {
        return ApiResponse.success(
                budgetService.updateBudget(budgetId, request)
        );
    }

    @DeleteMapping("/{budgetId}")
    public ApiResponse<String> deleteBudget(
            @PathVariable Long budgetId
    ) {
        budgetService.deleteBudget(budgetId);
        return ApiResponse.success("Budget deleted successfully");
    }


}

