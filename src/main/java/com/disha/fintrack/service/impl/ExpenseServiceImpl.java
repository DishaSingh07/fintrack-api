package com.disha.fintrack.service.impl;

import com.disha.fintrack.dto.ExpenseBreakdownItem;
import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.entity.CategoryEntity;
import com.disha.fintrack.entity.ExpenseEntity;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.mapper.ExpenseMapper;
import com.disha.fintrack.repository.CategoryRepository;
import com.disha.fintrack.repository.ExpenseRepository;
import com.disha.fintrack.service.ExpenseService;
import com.disha.fintrack.service.ProfileService;
import com.disha.fintrack.util.CategoryColorUtil;
import com.disha.fintrack.util.MonthDateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;
    private final ExpenseMapper mapper;

    @Override
    public ExpenseDTO addExpense(ExpenseDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ExpenseEntity newExpense = mapper.toEntity(dto);
        newExpense.setProfile(profile);
        newExpense.setCategory(category);
        ExpenseEntity savedExpense = expenseRepository.save(newExpense);
        return mapper.toDto(savedExpense);
    }

    @Override
    public ExpenseDTO getExpenseById(Long id) {
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return mapper.toDto(expense);
    }

    @Override
    public List<ExpenseDTO> getExpensesByProfileId(Long profileId) {
        return expenseRepository.findByProfileId(profileId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesByCategory(Long categoryId) {
        return expenseRepository.findByCategoryId(categoryId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO dto) {
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (dto.getAmount() != null) {
            expense.setAmount(dto.getAmount());
        }
        if (dto.getName() != null) {
            expense.setName(dto.getName());
        }
        if (dto.getIcon() != null) {
            expense.setIcon(dto.getIcon());
        }
        if (dto.getDate() != null) {
            expense.setDate(dto.getDate());
        }

        ExpenseEntity updatedExpense = expenseRepository.save(expense);
        return mapper.toDto(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        // ensure the expense exists for the current user
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expense.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepository.deleteById(id);
    }

    @Override
    public List<ExpenseDTO> getAllUserExpenses() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return getExpensesByProfileId(profile.getId());
    }

    @Override
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseDTO> expenses = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return expenses;
    }

    @Override
    public List<ExpenseDTO> getFiveLatestExpense() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalExpenseForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        return expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                        profile.getId(),
                        startDate,
                        endDate,
                        keyword,
                        sort)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesByDate(Long profileId, LocalDate date) {
        return expenseRepository.findByProfileIdAndDate(profileId, date)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    //    sum of expenses for a particular category id for the current month for the current user
    @Override
    public BigDecimal getCurrentMonthExpenseForCategory(Long categoryId) {
        ProfileEntity profile = profileService.getCurrentProfile();

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        return expenseRepository.findTotalExpenseForCategoryInMonth(
                profile.getId(),
                categoryId,
                startDate,
                endDate
        );
    }

    @Override
    public BigDecimal getSumForMonth(int month, int year) {
        Long profileId = profileService.getCurrentProfile().getId();
        LocalDate startDate = MonthDateMapper.getStartAndEndDate(month, year)[0];
        LocalDate endDate = MonthDateMapper.getStartAndEndDate(month, year)[1];

        return expenseRepository.sumExpenseBetweenDates(profileId, startDate, endDate);
    }

    public Map<String, Object> getExpenseBreakdown(int month, int year) {

        Long profileId = profileService.getCurrentProfile().getId();

        LocalDate startDate = MonthDateMapper.getStartAndEndDate(month, year)[0];
        LocalDate endDate = MonthDateMapper.getStartAndEndDate(month, year)[1];

        List<Object[]> rows =
                expenseRepository.getExpenseBreakdown(profileId, startDate, endDate);

        BigDecimal total = rows.stream()
                .map(r -> (BigDecimal) r[3])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ExpenseBreakdownItem> items = IntStream.range(0, rows.size())
                .mapToObj(i -> {
                    Object[] r = rows.get(i);
                    BigDecimal amount = (BigDecimal) r[3];

                    double percentage = total.compareTo(BigDecimal.ZERO) == 0
                            ? 0.0
                            : amount.multiply(BigDecimal.valueOf(100))
                            .divide(total, 2, RoundingMode.HALF_UP)
                            .doubleValue();

                    return ExpenseBreakdownItem.builder()
                            .categoryId((Long) r[0])
                            .categoryName((String) r[1])
                            .icon((String) r[2])
                            .color(CategoryColorUtil.getColor(i))
                            .amount(amount)
                            .percentage(percentage)
                            .build();
                })
                .toList();

        return Map.of(
                "items", items,
                "total", total
        );
    }

    public BigDecimal getTotalExpenseByCategoryForMonth(Long categoryId, Integer month, Integer year) {
        ProfileEntity profile = profileService.getCurrentProfile();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        BigDecimal total = expenseRepository.findTotalExpenseForCategoryInMonth(
                profile.getId(),
                categoryId,
                startDate,
                endDate
        );
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<ExpenseDTO> getExpenseForMonth(int month, int year) {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate[] dates = MonthDateMapper.getStartAndEndDate(month, year);
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];
        return expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}
