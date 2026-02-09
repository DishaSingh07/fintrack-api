package com.disha.fintrack.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SummaryResposne {

    private Period period;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private BigDecimal previousMonthIncome;
    private BigDecimal previousMonthExpense;
    private Double incomeTrend;
    private Double expenseTrend;
    private CategorySummary topExpenseCategory;
    private CategorySummary topIncomeCategory;


}


