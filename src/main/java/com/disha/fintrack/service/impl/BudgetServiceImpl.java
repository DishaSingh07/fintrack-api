package com.disha.fintrack.service.impl;

import com.disha.fintrack.dto.BudgetRequest;
import com.disha.fintrack.dto.BudgetResponse;
import com.disha.fintrack.entity.BudgetEntity;
import com.disha.fintrack.entity.CategoryEntity;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.enums.BudgetStatus;
import com.disha.fintrack.repository.BudgetRepository;
import com.disha.fintrack.repository.CategoryRepository;
import com.disha.fintrack.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final ProfileServiceImpl profileService;
    private final ExpenseServiceImpl expenseService;
    private final CategoryRepository categoryRepository;

    private static final Logger log = LoggerFactory.getLogger(BudgetServiceImpl.class);


    @Override
    public List<BudgetResponse> getAllBudgets(Integer month, Integer year) {
        Long profileId = profileService.getCurrentProfile().getId();


        List<BudgetEntity> budgets =
                (month == null || year == null)
                        ? budgetRepository.findByProfileId(profileId)
                        : budgetRepository.findByProfileIdAndMonthAndYear(profileId, month, year);

        return budgets.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BudgetResponse getBudget(Long budgetId) {
        Long profileId = profileService.getCurrentProfile().getId();

        BudgetEntity budget = budgetRepository
                .findByIdAndProfileId(budgetId, profileId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        return mapToResponse(budget);
    }

    @Override
    public BudgetResponse createBudget(BudgetRequest request) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!Objects.equals(category.getType(), "expense")) {
            throw new RuntimeException("Category must be of type EXPENSE");
        }

//        checking if budget for the category already exists for given month and year
        BudgetEntity existing = budgetRepository.findByProfileIdAndCategoryIdAndMonthAndYear(profile.getId(), request.getCategoryId(), request.getMonth(), request.getYear());

        if(existing != null)
        {
            existing.setLimitAmount(request.getLimit());
            return mapToResponse(budgetRepository.save(existing));
        }

        BudgetEntity budget = BudgetEntity.builder()
                .profile(profile)
                .category(category)
                .month(request.getMonth())
                .year(request.getYear())
                .limitAmount(request.getLimit())
                .build();

        return mapToResponse(budgetRepository.save(budget));
    }

    @Override
    public BudgetResponse updateBudget(Long budgetId, BudgetRequest request) {
        Long profileId = profileService.getCurrentProfile().getId();

        BudgetEntity budget = budgetRepository
                .findByIdAndProfileId(budgetId, profileId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (request.getLimit() != null) {
            budget.setLimitAmount(request.getLimit());
        }

        return mapToResponse(budgetRepository.save(budget));
    }

    @Override
    public void deleteBudget(Long budgetId) {
        Long profileId = profileService.getCurrentProfile().getId();

        BudgetEntity budget = budgetRepository
                .findByIdAndProfileId(budgetId, profileId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budgetRepository.delete(budget);
    }


    private BudgetResponse mapToResponse(BudgetEntity budget) {

        BigDecimal limit = budget.getLimitAmount();

        BigDecimal spent = expenseService.getTotalExpenseByCategoryForMonth(
                budget.getCategory().getId(),
                budget.getMonth(),
                budget.getYear()
        );

        spent = spent != null ? spent : BigDecimal.ZERO;

        BigDecimal remaining = limit.subtract(spent);
        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            remaining = BigDecimal.ZERO;
        }

        int percentage = limit.compareTo(BigDecimal.ZERO) > 0
                ? spent.multiply(BigDecimal.valueOf(100))
                .divide(limit, 0, RoundingMode.HALF_UP)
                .intValue()
                : 0;

        BudgetStatus status;
        if (percentage >= 100) {
            status = BudgetStatus.EXCEEDED;
        } else if (percentage >= 80) {
            status = BudgetStatus.WARNING;
        } else {
            status = BudgetStatus.ON_TRACK;
        }

        return BudgetResponse.builder()
                .id(budget.getId())
                .profileId(budget.getProfile().getId())
                .categoryId(budget.getCategory().getId())
                .month(budget.getMonth())
                .year(budget.getYear())
                .limit(limit)
                .spentAmount(spent)
                .remainingAmount(remaining)
                .percentage(percentage)
                .status(status)
                .createdAt(budget.getCreatedAt())
                .build();
    }
}
