package com.disha.fintrack.repository;

import com.disha.fintrack.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    List<ExpenseEntity> findByProfileId(Long profileId);

    List<ExpenseEntity> findByCategoryId(Long categoryId);

    //    select * from tbl_expenses where profile_id=? order by date desc;
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    //    select * from tbl_expenses where profile_id=? order by date desc limit 5;
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    //    sum of total expenses for a profile

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

    //    select * from tbl_expenses where profile_id=? and date=?
    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

    @Query("""
            SELECT e FROM ExpenseEntity e
            JOIN FETCH e.category
            WHERE e.profile.id = :profileId
            AND e.date = :date
            """)
    List<ExpenseEntity> findByProfileAndDateWithCategory(Long profileId, LocalDate date);


    @Query("""
                SELECT COALESCE(SUM(e.amount), 0)
                FROM ExpenseEntity e
                WHERE e.profile.id = :profileId
                  AND e.category.id = :categoryId
                  AND e.date BETWEEN :startDate AND :endDate
            """)
    BigDecimal findTotalExpenseForCategoryInMonth(
            @Param("profileId") Long profileId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM ExpenseEntity e
            WHERE e.profile.id = :profileId
              AND e.date BETWEEN :startDate AND :endDate
            """)
    BigDecimal sumExpenseBetweenDates(
            @Param("profileId") Long profileId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
                SELECT 
                    c.id,
                    c.name,
                    c.icon,
                    SUM(e.amount)
                FROM ExpenseEntity e
                JOIN e.category c
                WHERE e.profile.id = :profileId
                  AND e.date BETWEEN :startDate AND :endDate
                GROUP BY c.id, c.name, c.icon
            """)
    List<Object[]> getExpenseBreakdown(
            @Param("profileId") Long profileId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}

