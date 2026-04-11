package com.disha.fintrack.service;

import com.disha.fintrack.dto.TransactionDTO;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    public List<TransactionDTO> getRecentTransactions();

    void deleteTransaction(Long id);

    TransactionDTO updateTransaction(Long id, TransactionDTO dto);

    List<TransactionDTO> getTransactionsByCategory(Long categoryId);

    List<TransactionDTO> getTransactionsByProfileId(Long profileId, String type);

    TransactionDTO getTransactionById(Long id);

    TransactionDTO addTransaction(TransactionDTO dto);

    List<TransactionDTO> getCurrentMonthTransactionsForCurrentUser();

    BigDecimal getBalanceForCurrentMonthForCurrentUser();

    BigDecimal getTotalTransactionForCurrentUser();

    List<TransactionDTO> filterTransactions(LocalDate startDate, LocalDate endDate, String keyword, Sort sort,
            String type);

    BigDecimal getTotalTransactionForMonth(int month, int year, String type);

    BigDecimal getTotalTransactionByDates(LocalDate startDate, LocalDate endDate, String type);

    List<TransactionDTO> getTransactionForMonth(int month, int year);

}
