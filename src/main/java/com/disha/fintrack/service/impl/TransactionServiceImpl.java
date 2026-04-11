package com.disha.fintrack.service.impl;

import com.disha.fintrack.dto.TransactionDTO;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.entity.TransactionEntity;
import com.disha.fintrack.mapper.TransactionMapper;
import com.disha.fintrack.repository.TransactionRepository;
import com.disha.fintrack.service.ExpenseService;
import com.disha.fintrack.service.ProfileService;
import com.disha.fintrack.service.TransactionService;
import com.disha.fintrack.util.MonthDateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final ProfileService profileService;
    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    @Override
    public List<TransactionDTO> getRecentTransactions() {

        Long userId = profileService.getCurrentProfile().getId();

        List<TransactionEntity> entities = repository.findByProfileIdOrderByDateDesc(userId);

        // only taking 10 recent transactions
        return entities.stream()
                .limit(10)
                .map(mapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public void deleteTransaction(Long id) {
        ProfileEntity profile = profileService.getCurrentProfile();
        TransactionEntity transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!transaction.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this transaction");
        }
        repository.deleteById(id);
    }

    @Override
    public TransactionDTO updateTransaction(Long id, TransactionDTO dto) {
        TransactionEntity transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (dto.getAmount() != null) {
            transaction.setAmount(dto.getAmount());
        }
        if (dto.getName() != null) {
            transaction.setName(dto.getName());
        }
        if (dto.getDate() != null) {
            transaction.setDate(dto.getDate());
        }

        TransactionEntity updatedTransaction = repository.save(transaction);
        return mapper.toDto(updatedTransaction);
    }

    @Override
    public List<TransactionDTO> getTransactionsByCategory(Long categoryId) {
        return repository.findByCategoryId(categoryId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByProfileId(Long profileId, String type) {
        if (type != null) {
            return repository.findByProfileIdAndType(profileId, type)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findByProfileId(profileId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO getTransactionById(Long id) {
        TransactionEntity transaction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapper.toDto(transaction);
    }

    @Override
    public TransactionDTO addTransaction(TransactionDTO dto) {
        if (dto.getAmount() == null || dto.getName() == null || dto.getType() == null) {
            throw new RuntimeException("Amount, Name and Type are required");
        }
        ProfileEntity profile = profileService.getCurrentProfile();
        TransactionEntity newTransaction = mapper.toEntity(dto);
        newTransaction.setProfile(profile);
        TransactionEntity savedTransaction = repository.save(newTransaction);
        return mapper.toDto(savedTransaction);
    }

    @Override
    public List<TransactionDTO> getCurrentMonthTransactionsForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        return repository.findByProfileIdOrderByDateDesc(profile.getId())
                .stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionForMonth(int month, int year) {
        LocalDate[] dates = MonthDateMapper.getStartAndEndDate(month, year);
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];
        Long profileId = profileService.getCurrentProfile().getId();

        return repository.findByProfileIdAndDateBetween(profileId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> filterTransactions(LocalDate startDate, LocalDate endDate, String keyword, Sort sort,
            String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        return repository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseAndType(
                profile.getId(),
                startDate,
                endDate,
                keyword,
                type)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getBalanceForCurrentMonthForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        return repository.sumAmountBetweenDates(profile.getId(), startDate, endDate);
    }

    @Override
    public BigDecimal getTotalTransactionForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return repository.sumAmountUpToDate(profile.getId(), LocalDate.now());
    }

    @Override
    public BigDecimal getTotalTransactionForMonth(int month, int year, String type) {
        LocalDate[] dates = MonthDateMapper.getStartAndEndDate(month, year);
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];

        return getTotalTransactionByDates(startDate, endDate, type);
    }

    @Override
    public BigDecimal getTotalTransactionByDates(LocalDate startDate, LocalDate endDate, String type) {
        Long profileId = profileService.getCurrentProfile().getId();

        if (type.equalsIgnoreCase("INCOME")) {
            return repository.sumIncomeBetweenDates(profileId, startDate, endDate);
        } else if (type.equalsIgnoreCase("EXPENSE")) {
            return repository.sumExpenseBetweenDates(profileId, startDate, endDate);
        } else {
            return repository.sumAmountBetweenDates(profileId, startDate, endDate);
        }
    }

}
