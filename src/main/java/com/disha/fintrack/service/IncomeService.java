package com.disha.fintrack.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.dto.IncomeDTO;
import org.springframework.data.domain.Sort;

public interface IncomeService {
    List<IncomeDTO> getAllUserIncomes();

    void deleteIncome(Long id);

    IncomeDTO updateIncome(Long id, IncomeDTO dto);

    List<IncomeDTO> getIncomesByCategory(Long categoryId);

    List<IncomeDTO> getIncomesByProfileId(Long profileId);


    IncomeDTO getIncomeById(Long id);

    IncomeDTO addIncome(IncomeDTO dto);

    List<IncomeDTO> getCurrentMonthIncomesForCurrentUser();

    List<IncomeDTO> getFiveLatestIncome();

    BigDecimal getTotalIncomeForCurrentUser();

    List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

}


