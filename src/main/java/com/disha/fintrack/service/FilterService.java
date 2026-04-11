package com.disha.fintrack.service;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.dto.FilterDTO;
import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FilterService {

    private final TransactionService transactionService;

    public List<TransactionDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        return transactionService.filterTransactions(startDate, endDate, keyword, sort, "INCOME");
    }

    public List<TransactionDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        return transactionService.filterTransactions(startDate, endDate, keyword, sort, "EXPENSE");
    }

    public Object filterTransactions(FilterDTO filter) {
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction = "DESC".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        if ("income".equalsIgnoreCase(filter.getType())) {
            return filterIncomes(startDate, endDate, keyword, sort);
        } else if ("expense".equalsIgnoreCase(filter.getType())) {
            return filterExpenses(startDate, endDate, keyword, sort);
        } else {
            throw new IllegalArgumentException("Invalid type specified. Use 'income' or 'expense'.");
        }
    }


}