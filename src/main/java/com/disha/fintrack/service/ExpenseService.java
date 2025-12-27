package com.disha.fintrack.service;

import com.disha.fintrack.dto.ExpenseDTO;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseDTO addExpense(ExpenseDTO dto);

    ExpenseDTO getExpenseById(Long id);

    List<ExpenseDTO> getExpensesByProfileId(Long profileId);

    List<ExpenseDTO> getExpensesByCategory(Long categoryId);

    ExpenseDTO updateExpense(Long id, ExpenseDTO dto);

    void deleteExpense(Long id);

    List<ExpenseDTO> getAllUserExpenses();

    List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser();

    List<ExpenseDTO> getFiveLatestExpense();

    BigDecimal getTotalExpenseForCurrentUser();

    List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

//    notification
    List<ExpenseDTO> getExpensesByDate(Long profileId, LocalDate date);

}

