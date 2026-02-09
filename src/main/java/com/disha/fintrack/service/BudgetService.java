package com.disha.fintrack.service;

import com.disha.fintrack.dto.BudgetRequest;
import com.disha.fintrack.dto.BudgetResponse;

import java.util.List;

public interface BudgetService {

    List<BudgetResponse> getAllBudgets(Integer month, Integer year);

    BudgetResponse getBudget(Long budgetId);

    BudgetResponse createBudget(BudgetRequest request);

    BudgetResponse updateBudget(Long budgetId, BudgetRequest request);

    void deleteBudget(Long budgetId);
}
