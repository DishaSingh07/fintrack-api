package com.disha.fintrack.service;


import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.dto.RecentTransactionDTO;
import com.disha.fintrack.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProfileService profileService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<ExpenseDTO> latestExpenses = expenseService.getFiveLatestExpense();
        List<IncomeDTO> latestIncomes = incomeService.getFiveLatestIncome();
        BigDecimal totalExpenses = expenseService.getTotalExpenseForCurrentUser();
        BigDecimal totalIncomes = incomeService.getTotalIncomeForCurrentUser();

//        putting latest expenses and incomes in transactins
        List<RecentTransactionDTO> recentTransactions =
                Stream.concat(
                                latestIncomes.stream().map(income ->
                                        RecentTransactionDTO.builder()
                                                .id(income.getId())
                                                .profileId(profile.getId())
                                                .amount(income.getAmount())
                                                .date(income.getDate())
                                                .icon(income.getIcon())
                                                .name(income.getName())
                                                .createdAt(income.getCreatedAt())
                                                .updatedAt(income.getUpdatedAt())
                                                .type("INCOME")
                                                .build()
                                ),
                                latestExpenses.stream().map(expense ->
                                        RecentTransactionDTO.builder()
                                                .id(expense.getId())
                                                .profileId(profile.getId())
                                                .amount(expense.getAmount())
                                                .date(expense.getDate())
                                                .icon(expense.getIcon())
                                                .name(expense.getName())
                                                .createdAt(expense.getCreatedAt())
                                                .updatedAt(expense.getUpdatedAt())
                                                .type("EXPENSE")
                                                .build()
                                )
                        )
                        .sorted((a, b) -> {
                            int comp = b.getDate().compareTo(a.getDate());
                            if (comp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                                return b.getCreatedAt().compareTo(a.getCreatedAt());
                            }
                            return comp;
                        })
                        .collect(Collectors.toList());


        returnValue.put("totalBalance", totalIncomes.subtract(totalExpenses));
        returnValue.put("recentTransactions", recentTransactions.stream().limit(5).collect(Collectors.toList()));
        returnValue.put("totalIncomes", totalIncomes);
        returnValue.put("totalExpenses", totalExpenses);
        returnValue.put("recentfiveExpenses", latestExpenses);
        returnValue.put("recentfiveIncomes", latestIncomes);


        return returnValue;
    }
}
