package com.disha.fintrack.service;

import com.disha.fintrack.dto.*;
import com.disha.fintrack.util.MonthDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    private final FilterService filterService;
    private final TransactionService transactionService;

    public SummaryResposne generateMonthlySummary(int month, int year) {


        BigDecimal totalIncome = transactionService.getTotalTransactionForMonth(month, year, "INCOME");
        BigDecimal totalExpense = transactionService.getTotalTransactionForMonth(month, year, "EXPENSE");
        BigDecimal balance = totalIncome.subtract(totalExpense);

        LocalDate startDate = MonthDateMapper.getStartAndEndDate(month, year)[0];
        LocalDate endDate = MonthDateMapper.getStartAndEndDate(month, year)[1];

        int previousMonth = (month == 1) ? 12 : month - 1;
        int previousYear = (month == 1) ? year - 1 : year;

        BigDecimal previousMonthIncome = transactionService.getTotalTransactionForMonth(month, year, "INCOME");
        BigDecimal previousMonthExpense = transactionService.getTotalTransactionForMonth(month, year, "EXPENSE");

        double incomeTrend = calculateTrend(previousMonthIncome, totalIncome);
        double expenseTrend = calculateTrend(previousMonthExpense, totalExpense);

        // ✅ Now we get typed lists
        List<TransactionDTO> incomeList = filterService.filterIncomes(startDate, endDate, "", Sort.by(Sort.Direction.DESC, "amount"));
        if (incomeList.isEmpty()) {
            log.info("no income in the transactions found");
        }
        CategorySummary topIncomeCategory = extractTopIncomeCategory(incomeList);

        List<TransactionDTO> expenseList = filterService.filterExpenses(startDate, endDate, "", Sort.by(Sort.Direction.DESC, "amount"));
        if (expenseList.isEmpty()) {
            log.info("no expense in the transactions found");
        }
        CategorySummary topExpenseCategory = extractTopExpenseCategory(expenseList);

        return SummaryResposne.builder()
                .period(new Period(month, year))
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .previousMonthIncome(previousMonthIncome)
                .previousMonthExpense(previousMonthExpense)
                .incomeTrend(incomeTrend)
                .expenseTrend(expenseTrend)
                .topIncomeCategory(topIncomeCategory)
                .topExpenseCategory(topExpenseCategory)
                .build();
    }

    private CategorySummary extractTopIncomeCategory(List<TransactionDTO> incomes) {
        if (incomes.isEmpty()) {
            return new CategorySummary("N/A", 0.0);
        }
        TransactionDTO top = incomes.get(0);
        return new CategorySummary(top.getName(), top.getAmount().doubleValue());
    }

    private CategorySummary extractTopExpenseCategory(List<TransactionDTO> expenses) {
        if (expenses.isEmpty()) {
            return new CategorySummary("N/A", 0.0);
        }
        TransactionDTO top = expenses.get(0);
        return new CategorySummary(top.getName(), top.getAmount().doubleValue());
    }

    private double calculateTrend(BigDecimal previous, BigDecimal current) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) == 0 ? 0.0 : 100.0;
        }
        return current.subtract(previous)
                .multiply(BigDecimal.valueOf(100))
                .divide(previous, 2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}