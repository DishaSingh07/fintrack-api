package com.disha.fintrack.service;

import com.disha.fintrack.dto.TrendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public List<TrendDTO> getMonthlyTrends(int months) {

        List<TrendDTO> trendItems = new ArrayList<>();

        LocalDate now = LocalDate.now();

        for (int i = months - 1; i >= 0; i--) {

            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();

            BigDecimal income =
                    incomeService.getTotalIncomeForMonth(month, year);

            BigDecimal expense =
                    expenseService.getSumForMonth(month, year);

            double savingRate = calculateSavingRate(income, expense);

            trendItems.add(
                    TrendDTO.builder()
                            .month(date.getMonth().getDisplayName(
                                    TextStyle.FULL, Locale.ENGLISH
                            ) + " " + year)
                            .income(income.doubleValue())
                            .expense(expense.doubleValue())
                            .savingRate(savingRate)
                            .build()
            );
        }

        return trendItems;
    }

    private double calculateSavingRate(BigDecimal income, BigDecimal expense) {
        if (income.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        return income.subtract(expense)
                .multiply(BigDecimal.valueOf(100))
                .divide(income, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
