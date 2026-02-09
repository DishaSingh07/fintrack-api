package com.disha.fintrack.service.impl;

import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.dto.FilterDTO;
import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.dto.TransactionDTO;
import com.disha.fintrack.service.ExpenseService;
import com.disha.fintrack.service.IncomeService;
import com.disha.fintrack.service.ProfileService;
import com.disha.fintrack.service.TransactionService;
import com.google.gson.internal.Streams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;




    @Override
    public List<TransactionDTO> getRecentTransactions() {

        Long userId = profileService.getCurrentProfile().getId();

        return Stream.concat(
                        incomeService.getFiveLatestIncome()
                                .stream()
                                .map(income -> mapIncome(income, userId)),

                        expenseService.getFiveLatestExpense()
                                .stream()
                                .map(expense -> mapExpense(expense, userId))
                )
                .sorted(Comparator
                        .comparing(TransactionDTO::getDate).reversed()
                        .thenComparing(
                                TransactionDTO::getCreatedAt,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                )
                .limit(10)
                .toList();
    }


    private TransactionDTO mapIncome(IncomeDTO income, Long userId) {
        return TransactionDTO.builder()
                .id(income.getId())
                .date(income.getDate())
                .amount(income.getAmount())
                .category(income.getCategoryName())
                .title(income.getName())
                .type("INCOME")   // distinguish type
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .build();
    }

    private TransactionDTO mapExpense(ExpenseDTO expense, Long userId) {
        return TransactionDTO.builder()
                .id(expense.getId())
                .date(expense.getDate())
                .amount(expense.getAmount())
                .category(expense.getCategoryName())
                .title(expense.getName())
                .type("EXPENSE")   // distinguish type
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}
