package com.disha.fintrack.service.impl;

import com.disha.fintrack.dto.ExpenseDTO;
import com.disha.fintrack.dto.IncomeDTO;
import com.disha.fintrack.entity.CategoryEntity;
import com.disha.fintrack.entity.IncomeEntity;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.mapper.IncomeMapper;
import com.disha.fintrack.repository.CategoryRepository;
import com.disha.fintrack.repository.IncomeRepository;
import com.disha.fintrack.service.IncomeService;
import com.disha.fintrack.service.ProfileService;
import com.disha.fintrack.util.MonthDateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;
    private final IncomeMapper mapper;

    @Override
    public IncomeDTO addIncome(IncomeDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        IncomeEntity newIncome = mapper.toEntity(dto);
        newIncome.setProfile(profile);
        newIncome.setCategory(category);
        IncomeEntity savedIncome = incomeRepository.save(newIncome);
        return mapper.toDto(savedIncome);
    }

    @Override
    public IncomeDTO getIncomeById(Long id) {
        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        return mapper.toDto(income);
    }

    @Override
    public List<IncomeDTO> getIncomesByProfileId(Long profileId) {
        return incomeRepository.findByProfileId(profileId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncomeDTO> getIncomesByCategory(Long categoryId) {
        return incomeRepository.findByCategoryId(categoryId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public IncomeDTO updateIncome(Long id, IncomeDTO dto) {
        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (dto.getAmount() != null) {
            income.setAmount(dto.getAmount());
        }
        if (dto.getName() != null) {
            income.setName(dto.getName());
        }
        if (dto.getIcon() != null) {
            income.setIcon(dto.getIcon());
        }
        if (dto.getDate() != null) {
            income.setDate(dto.getDate());
        }

        IncomeEntity updatedIncome = incomeRepository.save(income);
        return mapper.toDto(updatedIncome);
    }

    @Override
    public void deleteIncome(Long id) {
//        ensure the current user owns the income before deletion
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!income.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepository.deleteById(id);
    }

    @Override
    public List<IncomeDTO> getAllUserIncomes() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return getIncomesByProfileId(profile.getId());
    }

    @Override
    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeDTO> incomes = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return incomes;
    }

    @Override
    public BigDecimal getTotalIncomeForCurrentMonthForCurrentUser() {
        return null;
    }


    @Override
    public List<IncomeDTO> getFiveLatestIncome() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalIncomeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        return incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
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
    public BigDecimal getTotalIncomeForMonth(int month, int year) {

        LocalDate [] dates = MonthDateMapper.getStartAndEndDate(month, year);
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];
        Long profileId = profileService.getCurrentProfile().getId();


        return incomeRepository.sumIncomeBetweenDates(profileId, startDate, endDate);
    }

    @Override
    public List<IncomeDTO> getIncomeForMonth(int month, int year) {
        LocalDate [] dates = MonthDateMapper.getStartAndEndDate(month, year);
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];
        Long profileId = profileService.getCurrentProfile().getId();

        return incomeRepository.findByProfileIdAndDateBetween(profileId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

