package com.disha.fintrack.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ExpenseBreakdownItem {


    private Long categoryId;
    private String categoryName;
    private String icon;
    private String color;
    private BigDecimal amount;
    private Double percentage;
}
