package com.disha.fintrack.dto;

import com.disha.fintrack.enums.BudgetStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class BudgetResponse {
    private Long id;
    private Long profileId;
    private Long categoryId;
    private Integer month;
    private Integer year;
    private BigDecimal limit;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Integer percentage;
    private BudgetStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
