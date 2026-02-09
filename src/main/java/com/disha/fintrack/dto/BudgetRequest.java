package com.disha.fintrack.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetRequest {
    private Long categoryId;
    private Integer month;
    private Integer year;
    private BigDecimal limit;
}
