package com.disha.fintrack.repository;

import com.disha.fintrack.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByProfileId(Long profileId);

    List<TransactionEntity> findByCategoryId(Long categoryId);

    List<TransactionEntity> findByProfileIdAndType(Long profileId, String type);

    List<TransactionEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<TransactionEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    @Query("""
            SELECT COALESCE(SUM(
            CASE
                WHEN e.type = 'INCOME' THEN e.amount
                WHEN e.type = 'EXPENSE' THEN -e.amount
                ELSE 0
            END
                ), 0)
            FROM TransactionEntity e
            WHERE e.profile.id = :profileId
              AND e.date BETWEEN :startDate AND :endDate
            """)
    BigDecimal sumAmountBetweenDates(@Param("profileId") Long profileId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // total balance amount
    @Query("""
            SELECT COALESCE(SUM(
            CASE
                WHEN e.type = 'INCOME' THEN e.amount
                WHEN e.type = 'EXPENSE' THEN -e.amount
                ELSE 0
            END
                ), 0)
            FROM TransactionEntity e
            WHERE e.profile.id = :profileId
              AND e.date <= :endDate
            """)
    BigDecimal sumAmountUpToDate(@Param("profileId") Long profileId, @Param("endDate") LocalDate endDate);

    // sum amount for income
    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM TransactionEntity e
            WHERE e.profile.id = :profileId
              AND e.date BETWEEN :startDate AND :endDate
              AND e.type = 'INCOME'
            """)
    BigDecimal sumIncomeBetweenDates(@Param("profileId") Long profileId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // sum amount for expense
    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM TransactionEntity e
            WHERE e.profile.id = :profileId
              AND e.date BETWEEN :startDate AND :endDate
              AND e.type = 'EXPENSE'
            """)
    BigDecimal sumExpenseBetweenDates(@Param("profileId") Long profileId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // total sum amount for income without date filter
    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM TransactionEntity e
            WHERE e.profile.id = :profileId
              AND e.type = 'INCOME'
            """)
    BigDecimal sumTotalIncome(@Param("profileId") Long profileId);

    // total sum amount for expense without date filter
    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM TransactionEntity e
            WHERE e.profile.id = :profileId
              AND e.type = 'EXPENSE'
            """)
    BigDecimal sumTotalExpense(@Param("profileId") Long profileId);


    
    // filtering out transactions based on date range and keyword and sorting and
    // type
    List<TransactionEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCaseAndType(Long profileId,
            LocalDate startDate, LocalDate endDate, String keyword, String type);
}
