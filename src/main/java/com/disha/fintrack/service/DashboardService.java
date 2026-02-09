package com.disha.fintrack.service;

import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.dto.TransactionDTO;
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
    private final TransactionService transactionService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
//        List<ExpenseDTO> latestExpenses = expenseService.getFiveLatestExpense();
//        List<IncomeDTO> latestIncomes = incomeService.getFiveLatestIncome();
        BigDecimal totalExpenses = expenseService.getTotalExpenseForCurrentUser();
        BigDecimal totalIncomes = incomeService.getTotalIncomeForCurrentUser();

        // putting latest expenses and incomes in transactins
        List<TransactionDTO> recentTransactions = transactionService.getRecentTransactions();

        returnValue.put("totalBalance", totalIncomes.subtract(totalExpenses));
        returnValue.put("recentTransactions", recentTransactions);
        returnValue.put("totalIncomes", totalIncomes);
        returnValue.put("totalExpenses", totalExpenses);
//        returnValue.put("recentfiveExpenses", latestExpenses);
//        returnValue.put("recentfiveIncomes", latestIncomes);

        return returnValue;
    }

    public Map<String, Object> getProfileSummary() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<ExpenseDTO> latestExpenses = expenseService.getFiveLatestExpense();
        List<IncomeDTO> latestIncomes = incomeService.getFiveLatestIncome();
        BigDecimal totalExpenses = expenseService.getTotalExpenseForCurrentUser();
        BigDecimal totalIncomes = incomeService.getTotalIncomeForCurrentUser();

        returnValue.put("totalIncomes", totalIncomes);
        returnValue.put("totalExpense", totalExpenses);
        returnValue.put("balance", totalIncomes.subtract(totalExpenses));

        return returnValue;

    }
}

