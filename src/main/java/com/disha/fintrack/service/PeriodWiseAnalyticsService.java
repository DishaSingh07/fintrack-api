package com.disha.fintrack.service;

import com.disha.fintrack.dto.ChartData;
import com.disha.fintrack.dto.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeriodWiseAnalyticsService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public List<ChartData> getPeriodWiseAnalytics(Period period) {
        log.info("Generating period-wise analytics for period: {}", period);
        List<ChartData> chartDataList = new ArrayList<>();

//            generating weekly data for the given month and year
        for (int week = 1; week <= 4; week++) {
            log.info("Processing week {} of period {}", week, period);
            LocalDate startDate = LocalDate.of(period.getYear(), period.getMonth(), (week - 1) * 7 + 1);
            LocalDate endDate = startDate.plusDays(6);
            log.info("Week {} start date: {}, end date: {}", week, startDate, endDate);
            Double income = incomeService.filterIncomes(startDate, endDate, "", null).stream()
                    .mapToDouble(incomeDTO -> incomeDTO.getAmount().doubleValue())
                    .sum();
            Double expense = expenseService.filterExpenses(startDate, endDate, "", null).stream()
                    .mapToDouble(expenseDTO -> expenseDTO.getAmount().doubleValue())
                    .sum();
            log.info("Week {} income: {}, expense: {}", week, income, expense);
            Double balance = income - expense;
            ChartData chartData = ChartData.builder()
                    .name("Week " + week)
                    .income(income)
                    .expense(expense)
                    .balance(balance)
                    .build();
            chartDataList.add(chartData);

        }

        // Placeholder implementation

      return chartDataList;
    }


}
