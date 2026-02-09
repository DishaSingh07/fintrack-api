package com.disha.fintrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "tbl_budgets",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"profile_id", "category_id", "month", "year"}
        )
)
public class BudgetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal limitAmount;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity profile;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @CreationTimestamp
    private Instant createdAt;
}
